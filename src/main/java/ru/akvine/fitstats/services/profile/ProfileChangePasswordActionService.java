package ru.akvine.fitstats.services.profile;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.profile.ProfileChangePasswordActionEntity;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.exceptions.security.*;
import ru.akvine.fitstats.repositories.profile.ProfileChangePasswordActionRepository;
import ru.akvine.fitstats.repositories.security.ActionRepository;
import ru.akvine.fitstats.services.PasswordService;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.profile.change_password.ProfileChangePasswordActionRequest;
import ru.akvine.fitstats.services.dto.profile.change_password.ProfileChangePasswordActionResult;
import ru.akvine.fitstats.services.dto.security.OtpCreateNewAction;
import ru.akvine.fitstats.services.security.PasswordRequiredActionService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileChangePasswordActionService extends PasswordRequiredActionService<ProfileChangePasswordActionEntity> {
    private final ProfileChangePasswordActionRepository profileChangePasswordActionRepository;
    private final PasswordService passwordService;

    @Value("${security.otp.action.lifetime.seconds}")
    private int otpActionLifetimeSeconds;
    @Value("${security.otp.max.invalid.attempts}")
    private int otpMaxInvalidAttempts;
    @Value("${security.otp.password.max.invalid.attempts}")
    private int otpPasswordMaxInvalidAttempts;

    public ProfileChangePasswordActionResult startChangePassword(ProfileChangePasswordActionRequest request) {
        Preconditions.checkNotNull(request, "request is null!");

        ClientBean client = clientService.getByUuid(request.getClientUuid());
        final boolean isPasswordValid = isValidPassword(client, request.getCurrentPassword());

        String login = client.getEmail();
        String newPassword = request.getNewPassword();
        String sessionId = request.getSessionId();

        ProfileChangePasswordActionEntity changePasswordAction = getRepository().findCurrentAction(login);
        if (changePasswordAction == null) {
            OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, newPassword, sessionId, isPasswordValid);
            return buildActionInfo(createNewActionAndSendOtp(otpCreateNewAction));
        }

        // Действие просрочено
        if (changePasswordAction.getOtpAction().isActionExpired()) {
            getRepository().delete(changePasswordAction);
            OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, newPassword, sessionId, isPasswordValid);
            return buildActionInfo(createNewActionAndSendOtp(otpCreateNewAction));
        }

        // otp не был сгенерирован т.к. вводили неправильный пароль
        if (changePasswordAction.getOtpAction().getOtpNumber() == null) {
            return buildActionInfo(updateNewOtpAndSendToClient(changePasswordAction));
        }

        // Действие не просрочено и код еще годен
        if (changePasswordAction.getOtpAction().isNotExpiredOtp()) {
            return buildActionInfo(changePasswordAction);
        }

        // Код просрочен, но новый сгенерить не можем - лимит исчерпан
        if (changePasswordAction.getOtpAction().isNewOtpLimitReached()) {
            handleNoMoreNewOtp(changePasswordAction);
            throw new NoMoreNewOtpAvailableException("No more one-time-password can be generated!");
        }

        // Действие не просрочено, но просрочен код, нужно сгенерировать новый - лимит еще есть
        return buildActionInfo(changePasswordAction);
    }

    public ProfileChangePasswordActionResult newOtpChangePassword(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        ClientBean client = clientService.getByUuid(clientUuid);
        String login = client.getEmail();
        return generateNewOtp(login);
    }

    @Override
    protected ProfileChangePasswordActionEntity createNewActionAndSendOtp(OtpCreateNewAction otpCreateNewAction) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime actionExpiredAt = now.plusSeconds(otpActionLifetimeSeconds);

        String newHash = passwordService.encodePassword(otpCreateNewAction.getNewValue());

        ProfileChangePasswordActionEntity changePasswordAction = new ProfileChangePasswordActionEntity();
        changePasswordAction.setLogin(otpCreateNewAction.getLogin());
        changePasswordAction.setSessionId(otpCreateNewAction.getSessionId());
        changePasswordAction.setNewHash(newHash);
        changePasswordAction.setPwdInvalidAttemptsLeft(otpPasswordMaxInvalidAttempts);

        OtpActionEntity otp = new OtpActionEntity()
                .setStartedDate(now)
                .setActionExpiredAt(actionExpiredAt)
                .setOtpInvalidAttemptsLeft(otpMaxInvalidAttempts)
                .setOtpCountLeft(otpMaxInvalidAttempts);
        changePasswordAction.setOtpAction(otp);

        return updateNewOtpAndSendToClient(changePasswordAction);
    }

    public void finishChangePassword(ProfileChangePasswordActionRequest profileChangePassword) {
        Preconditions.checkNotNull(profileChangePassword, "profileChangePassword is null");
        Preconditions.checkNotNull(profileChangePassword.getOtp(), "profileChangePassword.sessionId is null");

        ClientBean client = clientService.getByUuid(profileChangePassword.getClientUuid());
        String login = client.getEmail();
        String otp = profileChangePassword.getOtp();


        ProfileChangePasswordActionEntity profileChangePasswordAction = getRepository().findCurrentAction(login);
        if (profileChangePasswordAction == null) {
            logger.info("Client with email = {} tried to {}, but action is not started", login, getActionName());
            throw new ActionNotStartedException("Can`t finish " + getActionName() + ", action not initiated!");
        }

        // Действие просрочено
        if (profileChangePasswordAction.getOtpAction().isActionExpired()) {
            logger.info("Client with email = {} tired to finish action = {}, but action is expired", login, getActionName());
            getRepository().delete(profileChangePasswordAction);
            logger.info("Expired action = {}[id={}] removed from DB", getActionName(), profileChangePasswordAction.getId());
            throw new ActionNotStartedException("Can`t finish " + getActionName() + ", action is expired!");
        }

        // Действие не просрочено, но просрочен код
        if (profileChangePasswordAction.getOtpAction().isExpiredOtp()) {
            logger.info("Client with email = {} tried to finish action = {}, but otp is expired! New otp left = {}", login, getActionName(),
                    profileChangePasswordAction.getOtpAction().getOtpCountLeft());
            throw new OtpExpiredException(profileChangePasswordAction.getOtpAction().getOtpCountLeft());
        }

        // Действие не просрочено и код еще активен - проверяем
        if (profileChangePasswordAction.getOtpAction().isOtpValid(otp)) {
            String newPasswordHash = profileChangePasswordAction.getNewHash();
            clientService.updatePassword(login, newPasswordHash);
            logger.info("Client with email = {} successfully passed one-time-password and was updated his password", login);

            getRepository().delete(profileChangePasswordAction);
            return;
        }

        // Неверный ввод
        int otpInvalidAttemptsLeft = profileChangePasswordAction.getOtpAction().decrementInvalidAttemptsLeft();
        ProfileChangePasswordActionEntity updatedChangePasswordAction = getRepository().save(profileChangePasswordAction);
        if (otpInvalidAttemptsLeft == 0) {
            handleNoMoreOtpInvalidAttemptsLeft(updatedChangePasswordAction);
            throw new BlockedCredentialsException(login);
        }
        throw new OtpInvalidAttemptException(login, otpInvalidAttemptsLeft);
    }

    @Override
    protected String getActionName() {
        return "profile-change-password-action";
    }

    @Override
    protected ActionRepository<ProfileChangePasswordActionEntity> getRepository() {
        return profileChangePasswordActionRepository;
    }

    @Override
    protected void sendNewOtpToClient(ProfileChangePasswordActionEntity action) {
        String login = action.getLogin();
        String otpValue = action.getOtpAction().getOtpValue();
        notificationProvider.sendProfileChangePasswordCode(login, otpValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ProfileChangePasswordActionResult buildActionInfo(ProfileChangePasswordActionEntity action) {
        return new ProfileChangePasswordActionResult(action);
    }
}
