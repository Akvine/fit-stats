package ru.akvine.fitstats.services.profile;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.profile.ProfileChangeEmailActionEntity;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.exceptions.security.*;
import ru.akvine.fitstats.repositories.profile.ProfileChangeEmailActionRepository;
import ru.akvine.fitstats.repositories.security.ActionRepository;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.profile.change_email.ProfileChangeEmailActionResult;
import ru.akvine.fitstats.services.dto.profile.change_email.ProfileChangeEmailActionRequest;
import ru.akvine.fitstats.services.dto.security.OtpCreateNewAction;
import ru.akvine.fitstats.services.security.PasswordRequiredActionService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileChangeEmailActionService extends PasswordRequiredActionService<ProfileChangeEmailActionEntity> {
    private final ProfileChangeEmailActionRepository profileChangeEmailActionRepository;

    @Value("${security.otp.action.lifetime.seconds}")
    private int otpActionLifetimeSeconds;
    @Value("${security.otp.max.invalid.attempts}")
    private int otpMaxInvalidAttempts;
    @Value("${security.otp.max.new.generation.per.action}")
    private int otpMaxNewGenerationPerAction;

    public ProfileChangeEmailActionResult startChangeEmail(ProfileChangeEmailActionRequest profileChangeEmailActionRequest) {
        Preconditions.checkNotNull(profileChangeEmailActionRequest, "profileChangeEmailActionStart is null");

        String clientUuid = profileChangeEmailActionRequest.getClientUuid();
        ClientBean clientBean = clientService.getByUuid(clientUuid);
        final boolean isPasswordValid = isValidPassword(clientBean, profileChangeEmailActionRequest.getPassword());

        String login = clientBean.getEmail();
        String sessionId = profileChangeEmailActionRequest.getSessionId();
        String newEmail = profileChangeEmailActionRequest.getNewEmail();

        ProfileChangeEmailActionEntity profileChangeEmailActionEntity = getRepository().findCurrentAction(login);
        if (profileChangeEmailActionEntity == null) {
            OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, newEmail, sessionId, isPasswordValid);
            return buildActionInfo(createNewActionAndSendOtp(otpCreateNewAction));
        }

        // Действие просрочено
        if (profileChangeEmailActionEntity.getOtpAction().isActionExpired()) {
            getRepository().delete(profileChangeEmailActionEntity);
            OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, newEmail, sessionId, isPasswordValid);
            return buildActionInfo(createNewActionAndSendOtp(otpCreateNewAction));
        }

        // otp не был сгенерирован, т.к. вводили неправильный пароль
        if (profileChangeEmailActionEntity.getOtpAction().getOtpNumber() == null) {
            return buildActionInfo(updateNewOtpAndSendToClient(profileChangeEmailActionEntity));
        }

        // Действие не просрочено и код еще годен
        if (profileChangeEmailActionEntity.getOtpAction().isNotExpiredOtp()) {
            return buildActionInfo(profileChangeEmailActionEntity);
        }

        // Код просрочен, но новый сгенерировать не можем - лимит исчерпан
        if (profileChangeEmailActionEntity.getOtpAction().isNewOtpLimitReached()) {
            handleNoMoreNewOtp(profileChangeEmailActionEntity);
            throw new NoMoreNewOtpAvailableException("No more one-time-password can be generated!");
        }

        return buildActionInfo(profileChangeEmailActionEntity);
    }

    public ProfileChangeEmailActionResult newOtpEmailChange(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");

        ClientBean clientBean = clientService.getByUuid(clientUuid);
        String login = clientBean.getEmail();
        return generateNewOtp(login);
    }

    public void finishEmailChange(ProfileChangeEmailActionRequest profileChangeEmailActionRequest) {
        Preconditions.checkNotNull(profileChangeEmailActionRequest, "profileChangeEmailActionStart is null");

        String clientUuid = profileChangeEmailActionRequest.getClientUuid();
        ClientBean clientBean = clientService.getByUuid(clientUuid);
        String otp = profileChangeEmailActionRequest.getOtp();
        String login = clientBean.getEmail();

        ProfileChangeEmailActionEntity profileChangeEmailActionEntity = getRepository().findCurrentAction(login);
        if (profileChangeEmailActionEntity == null) {
            throw new ActionNotStartedException(String.format("Can't finish %s, action not initiated!", getActionName()));
        }

        // Действие просрочено
        if (profileChangeEmailActionEntity.getOtpAction().isActionExpired()) {
            getRepository().delete(profileChangeEmailActionEntity);
            throw new ActionNotStartedException(String.format("Can't finish %s, action is expired!", getActionName()));
        }

        // Действие не просрочено, но просрочен код
        if (profileChangeEmailActionEntity.getOtpAction().isExpiredOtp()) {
            throw new OtpExpiredException(profileChangeEmailActionEntity.getOtpAction().getOtpCountLeft());
        }

        // Действие не просрочено и код еще активен - проверяем
        if (profileChangeEmailActionEntity.getOtpAction().isOtpValid(otp)) {
            String newEmail = getRepository().findCurrentAction(login).getNewEmail();
            clientService.updateEmail(login, newEmail);
            getRepository().delete(profileChangeEmailActionEntity);
            return;
        }

        int otpInvalidAttemptsLeft = profileChangeEmailActionEntity.getOtpAction().decrementInvalidAttemptsLeft();
        getRepository().save(profileChangeEmailActionEntity);
        if (otpInvalidAttemptsLeft == 0) {
            handleNoMoreNewOtp(profileChangeEmailActionEntity);
            throw new BlockedCredentialsException(login);
        }
        throw new OtpInvalidAttemptException(login, otpInvalidAttemptsLeft);
    }

    @Override
    protected ProfileChangeEmailActionEntity createNewActionAndSendOtp(OtpCreateNewAction otpCreateNewAction) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime actionExpiredAt = now.plusSeconds(otpActionLifetimeSeconds);

        ProfileChangeEmailActionEntity changeEmailActionEntity = new ProfileChangeEmailActionEntity()
                .setLogin(otpCreateNewAction.getLogin())
                .setSessionId(otpCreateNewAction.getSessionId())
                .setNewEmail(otpCreateNewAction.getNewValue())
                .setPwdInvalidAttemptsLeft(otpMaxInvalidAttempts);

        OtpActionEntity otp = new OtpActionEntity()
                .setStartedDate(now)
                .setActionExpiredAt(actionExpiredAt)
                .setOtpInvalidAttemptsLeft(otpMaxInvalidAttempts)
                .setOtpCountLeft(otpMaxNewGenerationPerAction);
        changeEmailActionEntity.setOtpAction(otp);

        return updateNewOtpAndSendToClient(changeEmailActionEntity);
    }

    @Override
    protected String getActionName() {
        return "profile-change-email-action";
    }

    @Override
    protected ActionRepository<ProfileChangeEmailActionEntity> getRepository() {
        return profileChangeEmailActionRepository;
    }

    @Override
    protected void sendNewOtpToClient(ProfileChangeEmailActionEntity action) {
        String login = action.getLogin();
        String otpValue = action.getOtpAction().getOtpValue();
        notificationProvider.sendProfileChangeEmailCode(login, otpValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ProfileChangeEmailActionResult buildActionInfo(ProfileChangeEmailActionEntity action) {
        return new ProfileChangeEmailActionResult(action);
    }
}