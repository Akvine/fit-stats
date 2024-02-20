package ru.akvine.fitstats.services.security;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.security.AccessRestoreActionEntity;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.entities.security.OtpInfo;
import ru.akvine.fitstats.enums.ActionState;
import ru.akvine.fitstats.exceptions.security.*;
import ru.akvine.fitstats.repositories.security.AccessRestoreActionRepository;
import ru.akvine.fitstats.repositories.security.ActionRepository;
import ru.akvine.fitstats.services.client.ClientService;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.security.access_restore.AccessRestoreActionRequest;
import ru.akvine.fitstats.services.dto.security.access_restore.AccessRestoreActionResult;
import ru.akvine.fitstats.services.properties.PropertyParseService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessRestoreActionService extends OtpActionService<AccessRestoreActionEntity> {
    @Value("security.otp.action.lifetime.seconds")
    private String otpActionLifetimeSeconds;
    @Value("security.otp.max.invalid.attempts")
    private String optMaxInvalidAttempts;
    @Value("security.otp.max.new.generation.per.action")
    private String otpMaxNewGenerationPerAction;

    private final AccessRestoreActionRepository accessRestoreActionRepository;
    private final ClientService clientService;
    private final PropertyParseService propertyParseService;

    public AccessRestoreActionResult startAccessRestore(AccessRestoreActionRequest accessRestoreActionRequest) {
        Preconditions.checkNotNull(accessRestoreActionRequest, "accessRestoreActionRequest is null");

        String login = accessRestoreActionRequest.getLogin();
        String sessionId = accessRestoreActionRequest.getSessionId();

        verifyNotBlocked(login);

        AccessRestoreActionEntity accessRestoreAction = accessRestoreActionRepository.findCurrentAction(login);
        if (accessRestoreAction == null) {
            return buildActionInfo(createNewActionAndSendOtp(login, sessionId));
        }
        // Действие просрочено
        if (accessRestoreAction.getOtpAction().isActionExpired()) {
            accessRestoreActionRepository.delete(accessRestoreAction);
            return buildActionInfo(createNewActionAndSendOtp(login, sessionId));
        }
        // Код уже был введен - все корректно
        if (isInFinalState(accessRestoreAction)) {
            return buildActionInfo(accessRestoreAction);
        }
        // Действие не просрочено и код еще годен
        if (accessRestoreAction.getOtpAction().isNotExpiredOtp()) {
            return buildActionInfo(accessRestoreAction);
        }
        // Код просрочен, но новый сгенерровать не можем - лимит исчерпан
        if (accessRestoreAction.getOtpAction().isNewOtpLimitReached()) {
            handleNoMoreNewOtp(accessRestoreAction);
            throw new NoMoreNewOtpAvailableException("No more one-time-password can be generated!");
        }
        // Действие не просрочено, но просрочен код, нужно сгенерировать новый - лимит еще есть
        return buildActionInfo(updateNewOtpAndSendToClient(accessRestoreAction));
    }

    public AccessRestoreActionResult generateNewOneTimePassword(AccessRestoreActionRequest request) {
        Preconditions.checkNotNull(request, "accessRestoreActionRequest is null");
        Preconditions.checkNotNull(request.getLogin(), "accessRestoreActionRequest.login is null");
        Preconditions.checkNotNull(request.getSessionId(), "accessRestoreActionRequest.sessionId is null");
        return generateNewOtp(request.getLogin());
    }

    public AccessRestoreActionResult checkOneTimePassword(AccessRestoreActionRequest actionRequest) {
        Preconditions.checkNotNull(actionRequest, "accessRestoreActionRequest is null");

        String login = actionRequest.getLogin();
        String otpValue = actionRequest.getOtp();

        AccessRestoreActionEntity accessRestoreAction = accessRestoreActionRepository.findCurrentAction(login);
        if (accessRestoreAction == null) {
            logger.info("Client tried to check otp of {}, but action is not started", getActionName());
            throw new ActionNotStartedException(String.format("Can't be check otp of %s, action not initiated!", getActionName()));
        }

        verifyNotBlocked(login);

        // Действие просрочено
        if (accessRestoreAction.getOtpAction().isActionExpired()) {
            logger.info("Client with email = {} tried to check otp {}, but action is expired", login, getActionName());
            accessRestoreActionRepository.delete(accessRestoreAction);
            throw new ActionNotStartedException(String.format("Can't check otp of %s, action not initiated", getActionName()));
        }

        // Код уже был введен - все корректно
        if (isInFinalState(accessRestoreAction)) {
            return buildActionInfo(accessRestoreAction);
        }

        if (noCurrentOtpInfoAvailable(accessRestoreAction)) {
            return buildActionInfo(accessRestoreAction);
        }

        // Действие не просрочено, но просрочен код
        if (accessRestoreAction.getOtpAction().isExpiredOtp()) {
            logger.info("Client with email = {} tried to check otp {}, but otp is expired! New otp left = {}", login,
                    getActionName(), accessRestoreAction.getOtpAction().getOtpValue());
            throw new OtpExpiredException(accessRestoreAction.getOtpAction().getOtpCountLeft());
        }

        // Действие не просрочено и код еще активен - проверяем
        if (accessRestoreAction.getOtpAction().isOtpValid(otpValue)) {
            logger.debug("Client with email = {} successfully passed otp for state = {}", login, accessRestoreAction.getState());
            return buildActionInfo(handleValidOtp(accessRestoreAction));
        }

        // Неверный ввод
        int otpInvalidAttemptsLeft = accessRestoreAction.getOtpAction().decrementInvalidAttemptsLeft();
        AccessRestoreActionEntity updatedPasswordChangeAction = accessRestoreActionRepository.save(accessRestoreAction);
        if (otpInvalidAttemptsLeft == 0) {
            handleNoMoreOtpInvalidAttemptsLeft(updatedPasswordChangeAction);
            throw new BlockedCredentialsException(login);
        }
        throw new OtpInvalidAttemptException(login, otpInvalidAttemptsLeft);
    }

    public ClientBean finishAccessRestore(AccessRestoreActionRequest actionRequest) {
        Preconditions.checkNotNull(actionRequest, "actionRestoreRequest is null");

        String login = actionRequest.getLogin();
        String password = actionRequest.getPassword();

        AccessRestoreActionEntity accessRestoreAction = accessRestoreActionRepository.findCurrentAction(login);
        if (accessRestoreAction == null) {
            logger.info("Client tried to finish {}, but action is not started", getActionName());
            throw new ActionNotStartedException(String.format("Can't finish %s, action not initiated", getActionName()));
        }

        verifyNotBlocked(login);

        // Действие просрочено
        if (accessRestoreAction.getOtpAction().isActionExpired()) {
            logger.info("Client with email = {} tried to finish {}, but action is expired!", login, getActionName());
            accessRestoreActionRepository.delete(accessRestoreAction);
            logger.trace("Expired {}[id={}] removed from DB", getActionName(), accessRestoreAction.getId());
            throw new ActionNotStartedException(String.format("Can't finish %s, action not initiated", getActionName()));
        }

        if (accessRestoreAction.getState() == ActionState.OTP_PASSED) {
            accessRestoreActionRepository.delete(accessRestoreAction);
            return clientService.updatePassword(login, password);
        }

        throw new OtpAuthRequiredException(String.format("Can't finish %s, otp auth required!", getActionName()));
    }

    @Override
    protected String getActionName() {
        return "access-restore-action";
    }

    @Override
    protected ActionRepository<AccessRestoreActionEntity> getRepository() {
        return accessRestoreActionRepository;
    }

    @Override
    protected void sendNewOtpToClient(AccessRestoreActionEntity action) {
        String login = action.getLogin();
        String otpValue = action.getOtpAction().getOtpValue();
        Integer otpNumber = action.getOtpAction().getOtpNumber();

        notificationProvider.sendAccessRestoreCode(login, otpValue);
        logger.info("Otp №{} for {} has been send to client with email = {}", otpNumber, getActionName(), login);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected AccessRestoreActionResult buildActionInfo(AccessRestoreActionEntity action) {
        return new AccessRestoreActionResult(action);
    }

    private AccessRestoreActionEntity createNewActionAndSendOtp(String login, String sessionId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime actionExpiredAt = now.plusSeconds(propertyParseService.parseInteger(otpActionLifetimeSeconds));

        OtpInfo otpInfo = otpService.getOneTimePassword(login);
        OtpActionEntity otpAction = new OtpActionEntity()
                .setStartedDate(now)
                .setActionExpiredAt(actionExpiredAt)
                .setOtpInvalidAttemptsLeft(propertyParseService.parseInteger(optMaxInvalidAttempts))
                .setOtpCountLeft(propertyParseService.parseInteger(otpMaxNewGenerationPerAction))
                .setNewOtpValue(otpInfo, now);

        AccessRestoreActionEntity accessRestoreAction = new AccessRestoreActionEntity()
                .setLogin(login)
                .setSessionId(sessionId)
                .setState(ActionState.NEW)
                .setOtpAction(otpAction);
        notificationProvider.sendAccessRestoreCode(login, accessRestoreAction.getOtpAction().getOtpValue());
        logger.info("Client with email = {} started new {}", login, getActionName());
        return accessRestoreActionRepository.save(accessRestoreAction);
    }

    private AccessRestoreActionEntity handleValidOtp(AccessRestoreActionEntity accessRestoreAction) {
        accessRestoreAction.setState(ActionState.OTP_PASSED);
        accessRestoreAction.getOtpAction().setOtpValueToNull();
        return accessRestoreActionRepository.save(accessRestoreAction);
    }

    private boolean isInFinalState(AccessRestoreActionEntity accessRestoreAction) {
        return accessRestoreAction.getState() == ActionState.OTP_PASSED;
    }
}
