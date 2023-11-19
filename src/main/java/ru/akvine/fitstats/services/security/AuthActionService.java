package ru.akvine.fitstats.services.security;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.security.AuthActionEntity;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.exceptions.security.NoMoreNewOtpAvailableException;
import ru.akvine.fitstats.repositories.security.ActionRepository;
import ru.akvine.fitstats.repositories.security.AuthActionRepository;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.security.OtpCreateNewAction;
import ru.akvine.fitstats.services.dto.security.auth.AuthActionRequest;
import ru.akvine.fitstats.services.dto.security.auth.AuthActionResult;
import ru.akvine.fitstats.services.notification.NotificationProvider;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthActionService extends PasswordRequiredActionService<AuthActionEntity> {
    private final AuthActionRepository authActionRepository;
    private final NotificationProvider notificationProvider;

    @Value("${security.otp.action.lifetime.seconds}")
    private int otpActionLifetimeSeconds;
    @Value("${security.otp.password.max.invalid.attempts}")
    private int otpPasswordMaxInvalidAttempts;
    @Value("${security.otp.max.invalid.attempts}")
    private int otpMaxInvalidAttempts;
    @Value("${security.otp.max.new.generation.per.action}")
    private int otpMaxNewGenerationPerAction;

    public AuthActionResult startAuth(AuthActionRequest request) {
        Preconditions.checkNotNull(request, "authActionRequest is null");

        String login = request.getLogin();
        String password = request.getPassword();
        String sessionId = request.getSessionId();

        verifyNotBlocked(login);
        ClientBean clientBean = clientService.getByEmail(login);
        final boolean isPasswordValid = isValidPassword(clientBean, password);

        AuthActionEntity authActionEntity = getRepository().findCurrentAction(login);
        if (authActionEntity == null) {
            OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, sessionId, isPasswordValid);
            return buildActionInfo(createNewActionAndSendOtp(otpCreateNewAction));
        }

        // Действие просрочено
        if (authActionEntity.getOtpAction().isActionExpired()) {
            getRepository().delete(authActionEntity);
            OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, sessionId, isPasswordValid);
            return buildActionInfo(createNewActionAndSendOtp(otpCreateNewAction));
        }
        if (!isPasswordValid) {
            handleInvalidPasswordInput(authActionEntity);
        }
        // otp не был сгенерирован, т.к. вводили неправильный пароль
        if (authActionEntity.getOtpAction().getOtpNumber() == null) {
            return buildActionInfo(updateNewOtpAndSend(authActionEntity));
        }
        // Действие не просрочено и код еще годен ... вернем текущее состояние
        if (authActionEntity.getOtpAction().isNotExpiredOtp()) {
            return buildActionInfo(authActionEntity);
        }
        // Код просрочен, но новый сгенерировать не можем - лимит исчерпан
        if (authActionEntity.getOtpAction().isNewOtpLimitReached()) {
            handleNoMoreNewOtp(authActionEntity);
            throw new NoMoreNewOtpAvailableException("No more one-time-password can be generated!");
        }
        // Действие не просрочено, но просрочен код, нужно сгенерировать новый - лимит еще есть
        return buildActionInfo(updateNewOtpAndSendToClient(authActionEntity));
    }

    public ClientBean finishAuth(AuthActionRequest request) {
        Preconditions.checkNotNull(request, "authActionRequest is null");

        String login = request.getLogin();
        String otp = request.getOtp();
        String sessionId = request.getOtp();

        verifyNotBlocked(login);
        ClientBean clientBean = clientService.getByEmail(login);
        AuthActionEntity authActionEntity = checkOtpInput(login, otp, sessionId);
        getRepository().delete(authActionEntity);
        return clientBean;
    }

    @Override
    protected AuthActionEntity createNewActionAndSendOtp(OtpCreateNewAction otpCreateNewAction) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime actionExpiredAt = now.plusSeconds(otpActionLifetimeSeconds);

        AuthActionEntity authActionEntity = new AuthActionEntity()
                .setLogin(otpCreateNewAction.getLogin())
                .setSessionId(otpCreateNewAction.getSessionId())
                .setPwdInvalidAttemptsLeft(otpPasswordMaxInvalidAttempts);

        OtpActionEntity otp = new OtpActionEntity()
                .setStartedDate(now)
                .setActionExpiredAt(actionExpiredAt)
                .setOtpInvalidAttemptsLeft(otpMaxInvalidAttempts)
                .setOtpCountLeft(otpMaxNewGenerationPerAction);
        authActionEntity.setOtpAction(otp);

        if (!otpCreateNewAction.isCredentialsValid()) {
            authActionEntity.decrementPwdInvalidAttemptsLeft();
            getRepository().save(authActionEntity);
            throw new BadCredentialsException("Invalid password");
        }
        return updateNewOtpAndSend(authActionEntity);
    }

    @Override
    protected String getActionName() {
        return "auth-action";
    }

    @Override
    protected ActionRepository<AuthActionEntity> getRepository() {
        return authActionRepository;
    }

    @Override
    protected void sendNewOtpToClient(AuthActionEntity action) {
        String login = action.getLogin();
        notificationProvider.sendAuthenticationCode(login, action.getOtpAction().getOtpValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected AuthActionResult buildActionInfo(AuthActionEntity action) {
        return new AuthActionResult(action);
    }
}
