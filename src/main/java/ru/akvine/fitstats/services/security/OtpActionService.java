package ru.akvine.fitstats.services.security;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.security.OneTimePasswordable;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.entities.security.OtpInfo;
import ru.akvine.fitstats.exceptions.security.*;
import ru.akvine.fitstats.repositories.security.ActionRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public abstract class OtpActionService<T extends OneTimePasswordable> {
    @Autowired
    protected BlockingService blockingService;
    @Autowired
    protected OtpService otpService;

    @Value("${security.otp.new.delay.seconds}")
    private long otpDelaySeconds;

    public <R> R generateNewOtp(String payload) {
        Preconditions.checkNotNull(payload, "payload is null");

        T action = getRepository().findCurrentAction(payload);
        if (action == null) {
            throw new ActionNotStartedException("Can'[t generate new one-time-password, action not started!");
        }

        verifyNotBlocked(action.getLogin());

        // Действие просрочено
        if (action.getOtpAction().isActionExpired()) {
            getRepository().delete(action);
            throw new ActionNotStartedException("Can't generate new one-time-password, action not started!");
        }

        if (noCurrentOtpInfoAvailable(action)) {
            return buildActionInfo(action);
        }

        // Действие не просрочено и не прошла задержка между генерациями
        if (newOtpDelayIsNotPassed(action)) {
            return buildActionInfo(action);
        }
        // Задержка прошла, но новый сгенерировать не можем - лимит исчерпан
        if (action.getOtpAction().isNewOtpLimitReached()) {
            handleNoMoreNewOtp(action);
            throw new NoMoreNewOtpAvailableException("No more one-time-password can be generatef!");
        }
        // Действие не просрчоено, задержка прошла, лимит не исчерпан - генериурем новый код
        return buildActionInfo(updateNewOtpAndSendToClient(action));
    }

    protected T checkOtpInput(String payload, String clientInput, String sessionId) {
        T action = getRepository().findCurrentAction(payload);
        if (action == null) {
            throw new ActionNotStartedException("Start registration action not started!");
        }

        verifySession(action, sessionId);

        String login = action.getLogin();
        // Действие просрочено
        if (action.getOtpAction().isActionExpired()) {
            getRepository().delete(action);
            throw new ActionNotStartedException(String.format("Can't finish %s, action is expired!", getActionName()));
        }

        // Действие не просрочено, но просрочен код
        if (action.getOtpAction().isExpiredOtp()) {
            throw new OtpExpiredException(action.getOtpAction().getOtpCountLeft());
        }
        // Действие не просрочено и код еще активен - проверяем
        if (!action.getOtpAction().isOtpValid(clientInput)) { // Неверный ввод
            int otpInvalidAttemptsLeft = action.getOtpAction().decrementInvalidAttemptsLeft();
            T updatedAction = getRepository().save(action);
            if (otpInvalidAttemptsLeft == 0) {
                handleNoMoreOtpInvalidAttemptsLeft(updatedAction);
                throw new BlockedCredentialsException(login);
            }

            handleOtpInvalidAttempt(login, otpInvalidAttemptsLeft);
        }

        return action;
    }

    protected void verifyNotBlocked(String login) {
        LocalDateTime unblockDate = blockingService.getUnblockDate(login);
        if (unblockDate == null || unblockDate.isBefore(LocalDateTime.now())) {
            return;
        }

        throw new BlockedCredentialsException(login);
    }

    protected void handleNoMoreNewOtp(T action) {
        String login = action.getLogin();
        blockingService.setBlock(login);
        getRepository().delete(action);
    }

    protected T updateNewOtpAndSendToClient(T action) {
        String login = action.getLogin();

        OtpInfo newOtpInfo = otpService.getOneTimePassword(login);
        action.getOtpAction().setNewOtpValue(newOtpInfo);

        sendNewOtpToClient(action);
        return getRepository().save(action);
    }

    protected abstract String getActionName();
    protected abstract ActionRepository<T> getRepository();
    protected abstract void sendNewOtpToClient(T action);
    protected abstract <R> R buildActionInfo(T action);

    protected final boolean noCurrentOtpInfoAvailable(T action) {
        OtpActionEntity otpAction = action.getOtpAction();
        return otpAction.getOtpNumber() == null
                || StringUtils.isBlank(otpAction.getOtpValue())
                || otpAction.getOtpExpiredAt() == null
                || otpAction.getOtpLastUpdate() == null;
    }

    private boolean newOtpDelayIsNotPassed(T passwordChangeAction) {
        LocalDateTime generationAllowedTime = passwordChangeAction.getOtpAction().getOtpLastUpdate().plusSeconds(otpDelaySeconds);
        return LocalDateTime.now().isBefore(generationAllowedTime);
    }

    protected void verifySession(T action, String sessionId) {
        String regSessionId = action.getSessionId();
        if (regSessionId == null) {
            return;
        }

        if (sessionId != null && sessionId.equals(regSessionId)) {
            return;
        }

        throw new WrongSessionException("Wrong session");
    }

    protected void handleNoMoreOtpInvalidAttemptsLeft(T action) {
        String login = action.getLogin();
        blockingService.setBlock(login);
        getRepository().delete(action);
    }

    protected void handleOtpInvalidAttempt(String login, int otpInvalidAttemptsLeft) {
        throw new OtpInvalidAttemptException(login, otpInvalidAttemptsLeft);
    }
}
