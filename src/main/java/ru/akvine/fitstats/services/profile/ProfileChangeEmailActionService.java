package ru.akvine.fitstats.services.profile;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.constants.DBLockPrefixesConstants;
import ru.akvine.fitstats.entities.profile.ProfileChangeEmailActionEntity;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.exceptions.security.*;
import ru.akvine.fitstats.repositories.profile.ProfileChangeEmailActionRepository;
import ru.akvine.fitstats.repositories.security.ActionRepository;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.profile.change_email.ProfileChangeEmailActionResult;
import ru.akvine.fitstats.services.dto.profile.change_email.ProfileChangeEmailActionRequest;
import ru.akvine.fitstats.services.dto.security.OtpCreateNewAction;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.services.security.PasswordRequiredActionService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileChangeEmailActionService extends PasswordRequiredActionService<ProfileChangeEmailActionEntity> {
    private final ProfileChangeEmailActionRepository profileChangeEmailActionRepository;
    private final PropertyParseService propertyParseService;

    @Value("security.otp.action.lifetime.seconds")
    private String otpActionLifetimeSeconds;
    @Value("security.otp.max.invalid.attempts")
    private String otpMaxInvalidAttempts;
    @Value("security.otp.max.new.generation.per.action")
    private String otpMaxNewGenerationPerAction;

    public ProfileChangeEmailActionResult startChangeEmail(ProfileChangeEmailActionRequest profileChangeEmailActionRequest) {
        Preconditions.checkNotNull(profileChangeEmailActionRequest, "profileChangeEmailActionStart is null");

        String clientUuid = profileChangeEmailActionRequest.getClientUuid();
        ClientBean clientBean = clientService.getByUuid(clientUuid);
        final boolean isPasswordValid = isValidPassword(clientBean, profileChangeEmailActionRequest.getPassword());

        String login = clientBean.getEmail();
        String sessionId = profileChangeEmailActionRequest.getSessionId();
        String newEmail = profileChangeEmailActionRequest.getNewEmail();

        ProfileChangeEmailActionEntity profileChangeEmailActionEntity = lockHelper.doWithLock(getLock(login), () -> {
            ProfileChangeEmailActionEntity profileChangeEmailAction = getRepository().findCurrentAction(login);
            if (profileChangeEmailAction == null) {
                OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, newEmail, sessionId, isPasswordValid);
                return createNewActionAndSendOtp(otpCreateNewAction);
            }

            // Действие просрочено
            if (profileChangeEmailAction.getOtpAction().isActionExpired()) {
                getRepository().delete(profileChangeEmailAction);
                OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, newEmail, sessionId, isPasswordValid);
                return createNewActionAndSendOtp(otpCreateNewAction);
            }

            // otp не был сгенерирован, т.к. вводили неправильный пароль
            if (profileChangeEmailAction.getOtpAction().getOtpNumber() == null) {
                return updateNewOtpAndSendToClient(profileChangeEmailAction);
            }

            // Действие не просрочено и код еще годен
            if (profileChangeEmailAction.getOtpAction().isNotExpiredOtp()) {
                return profileChangeEmailAction;
            }

            // Код просрочен, но новый сгенерировать не можем - лимит исчерпан
            if (profileChangeEmailAction.getOtpAction().isNewOtpLimitReached()) {
                handleNoMoreNewOtp(profileChangeEmailAction);
                throw new NoMoreNewOtpAvailableException("No more one-time-password can be generated!");
            }

            // Действие не просрочено, но просрочен код, нужно сгенерировать новый - лимит еще есть
            return updateNewOtpAndSendToClient(profileChangeEmailAction);
        });

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

        String lockId = getLock(login);
        lockHelper.doWithLock(lockId, () -> {
            ProfileChangeEmailActionEntity profileChangeEmailActionEntity = getRepository().findCurrentAction(login);
            if (profileChangeEmailActionEntity == null) {
                logger.info("Client with email = {} tried to finish action = {}, but action is not started!", login, getActionName());
                throw new ActionNotStartedException(String.format("Can't finish %s, action not initiated!", getActionName()));
            }

            // Действие просрочено
            if (profileChangeEmailActionEntity.getOtpAction().isActionExpired()) {
                logger.info("Client with email = {} tried to finish action = {}, but action is not started", login, getActionName());
                getRepository().delete(profileChangeEmailActionEntity);
                logger.info("Expired action = {}[id={}] removed from DB", getActionName(), profileChangeEmailActionEntity.getId());
                throw new ActionNotStartedException(String.format("Can't finish %s, action is expired!", getActionName()));
            }

            // Действие не просрочено, но просрочен код
            if (profileChangeEmailActionEntity.getOtpAction().isExpiredOtp()) {
                logger.info("Client with email = {} tried to finish action = {}, but otp is expired! New otp left = {}",
                        login, getActionName(), profileChangeEmailActionEntity.getOtpAction().getOtpCountLeft());
                throw new OtpExpiredException(profileChangeEmailActionEntity.getOtpAction().getOtpCountLeft());
            }

            // Действие не просрочено и код еще активен - проверяем
            if (profileChangeEmailActionEntity.getOtpAction().isOtpValid(otp)) {
                String newEmail = getRepository().findCurrentAction(login).getNewEmail();
                clientService.updateEmail(login, newEmail);
                logger.info("Client with email = {} successfully passed one-time-password and was updated his email", login);

                getRepository().delete(profileChangeEmailActionEntity);
                return true;
            }

            int otpInvalidAttemptsLeft = profileChangeEmailActionEntity.getOtpAction().decrementInvalidAttemptsLeft();
            getRepository().save(profileChangeEmailActionEntity);
            if (otpInvalidAttemptsLeft == 0) {
                handleNoMoreNewOtp(profileChangeEmailActionEntity);
                throw new BlockedCredentialsException(login);
            }
            throw new OtpInvalidAttemptException(login, otpInvalidAttemptsLeft);
        });
    }

    @Override
    protected ProfileChangeEmailActionEntity createNewActionAndSendOtp(OtpCreateNewAction otpCreateNewAction) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime actionExpiredAt = now.plusSeconds(propertyParseService.parseInteger(otpActionLifetimeSeconds));

        ProfileChangeEmailActionEntity changeEmailActionEntity = new ProfileChangeEmailActionEntity()
                .setLogin(otpCreateNewAction.getLogin())
                .setSessionId(otpCreateNewAction.getSessionId())
                .setNewEmail(otpCreateNewAction.getNewValue())
                .setPwdInvalidAttemptsLeft(propertyParseService.parseInteger(otpMaxInvalidAttempts));

        OtpActionEntity otp = new OtpActionEntity()
                .setStartedDate(now)
                .setActionExpiredAt(actionExpiredAt)
                .setOtpInvalidAttemptsLeft(propertyParseService.parseInteger(otpMaxInvalidAttempts))
                .setOtpCountLeft(propertyParseService.parseInteger(otpMaxNewGenerationPerAction));
        changeEmailActionEntity.setOtpAction(otp);

        return updateNewOtpAndSendToClient(changeEmailActionEntity);
    }

    @Override
    protected String getActionName() {
        return "profile-change-email-action";
    }

    @Override
    protected String getLock(String payload) {
        return DBLockPrefixesConstants.PROFILE_CHANGE_EMAIL_PREFIX + payload;
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
