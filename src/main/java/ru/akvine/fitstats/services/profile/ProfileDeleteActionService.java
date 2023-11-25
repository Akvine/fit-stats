package ru.akvine.fitstats.services.profile;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.profile.ProfileDeleteActionEntity;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.exceptions.security.*;
import ru.akvine.fitstats.repositories.profile.ProfileDeleteActionRepository;
import ru.akvine.fitstats.repositories.security.ActionRepository;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.profile.delete.ProfileDeleteActionRequest;
import ru.akvine.fitstats.services.dto.profile.delete.ProfileDeleteActionResult;
import ru.akvine.fitstats.services.dto.security.OtpCreateNewAction;
import ru.akvine.fitstats.services.security.PasswordRequiredActionService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileDeleteActionService extends PasswordRequiredActionService<ProfileDeleteActionEntity> {
    private final ProfileDeleteActionRepository profileDeleteActionRepository;

    @Value("${security.otp.action.lifetime.seconds}")
    private int otpActionLifetimeSeconds;
    @Value("${security.otp.max.invalid.attempts}")
    private int otpMaxInvalidAttempts;
    @Value("${security.otp.max.new.generation.per.action}")
    private int otpMaxNewGenerationPerAction;

    public ProfileDeleteActionResult startDelete(ProfileDeleteActionRequest profileDeleteActionRequest) {
        Preconditions.checkNotNull(profileDeleteActionRequest, "profileDeleteActionRequest is null");
        Preconditions.checkNotNull(profileDeleteActionRequest.getClientUuid(), "profileDeleteActionRequest.clientUuid is null");
        Preconditions.checkNotNull(profileDeleteActionRequest.getSessionId(), "profileDeleteActionRequest.sessionId is null");

        String clientUuid = profileDeleteActionRequest.getClientUuid();
        String sessionId = profileDeleteActionRequest.getSessionId();
        ClientBean clientBean = clientService.getByUuid(clientUuid);
        String login = clientBean.getEmail();

        ProfileDeleteActionEntity profileDeleteActionEntity = getRepository().findCurrentAction(login);
        if (profileDeleteActionEntity == null) {
            OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, sessionId, true);
            return buildActionInfo(createNewActionAndSendOtp(otpCreateNewAction));
        }

        // Действие просрочено
        if (profileDeleteActionEntity.getOtpAction().isActionExpired()) {
            getRepository().delete(profileDeleteActionEntity);
            OtpCreateNewAction otpCreateNewAction = new OtpCreateNewAction(login, sessionId, true);
            return buildActionInfo(createNewActionAndSendOtp(otpCreateNewAction));
        }

        // otp не был сгенерирован, т.к. вводили неправильный пароль
        if (profileDeleteActionEntity.getOtpAction().getOtpNumber() == null) {
            return buildActionInfo(updateNewOtpAndSendToClient(profileDeleteActionEntity));
        }

        // Действие не просрочено и код еще годен
        if (profileDeleteActionEntity.getOtpAction().isNotExpiredOtp()) {
            return buildActionInfo(profileDeleteActionEntity);
        }

        // Код просрочен, но новый сгенерировать не можем - лимит исчерпан
        if (profileDeleteActionEntity.getOtpAction().isNewOtpLimitReached()) {
            handleNoMoreNewOtp(profileDeleteActionEntity);
            throw new NoMoreNewOtpAvailableException("No more one-time-password can be generated!");
        }

        // Действие не просрочен, но просрочен код, нужно сгенерировать новый - лимит еще есть
        return buildActionInfo(updateNewOtpAndSendToClient(profileDeleteActionEntity));
    }

    public ProfileDeleteActionResult newOtpProfileDelete(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        ClientBean clientBean = clientService.getByUuid(clientUuid);
        String email = clientBean.getEmail();
        return generateNewOtp(email);
    }

    public void finishDelete(ProfileDeleteActionRequest profileDeleteActionRequest) {
        Preconditions.checkNotNull(profileDeleteActionRequest, "profileDeleteActionRequest is null");
        Preconditions.checkNotNull(profileDeleteActionRequest.getClientUuid(), "profileDeleteActionRequest.clientUuid is null");
        Preconditions.checkNotNull(profileDeleteActionRequest.getOtp(), "profileDeleteActionRequest.otp is null");

        String clientUuid = profileDeleteActionRequest.getClientUuid();
        ClientBean clientBean = clientService.getByUuid(clientUuid);
        String otp = profileDeleteActionRequest.getOtp();
        String login = clientBean.getEmail();

        ProfileDeleteActionEntity deleteActionEntity = getRepository().findCurrentAction(login);
        if (deleteActionEntity == null) {
            throw new ActionNotStartedException(String.format("Can't finish %s, action not initiated!", getActionName()));
        }

        // Действие просрочено
        if (deleteActionEntity.getOtpAction().isActionExpired()) {
            getRepository().delete(deleteActionEntity);
            throw new ActionNotStartedException(String.format("Can't finish %s, action is expired!", getActionName()));
        }

        // Действие не просрочено, но просрочен код
        if (deleteActionEntity.getOtpAction().isExpiredOtp()) {
            throw new OtpExpiredException(deleteActionEntity.getOtpAction().getOtpCountLeft());
        }

        // Действие не просрочено и код еще активен - проверяем
        if (deleteActionEntity.getOtpAction().isOtpValid(otp)) {
            clientService.delete(login);
            getRepository().delete(deleteActionEntity);
        }

        int otpInvalidAttemptsLeft = deleteActionEntity.getOtpAction().decrementInvalidAttemptsLeft();
        ProfileDeleteActionEntity updatedDeleteAction = getRepository().save(deleteActionEntity);
        if (otpInvalidAttemptsLeft == 0) {
            handleNoMoreOtpInvalidAttemptsLeft(updatedDeleteAction);
            throw new BlockedCredentialsException(login);
        }
        throw new OtpInvalidAttemptException(login, otpInvalidAttemptsLeft);
    }

    @Override
    protected ProfileDeleteActionEntity createNewActionAndSendOtp(OtpCreateNewAction otpCreateNewAction) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime actionExpiredAt = now.plusSeconds(otpActionLifetimeSeconds);

        ProfileDeleteActionEntity profileDeleteActionEntity = new ProfileDeleteActionEntity()
                .setLogin(otpCreateNewAction.getLogin())
                .setSessionId(otpCreateNewAction.getSessionId())
                .setPwdInvalidAttemptsLeft(otpMaxInvalidAttempts);

        OtpActionEntity otp = new OtpActionEntity()
                .setStartedDate(now)
                .setActionExpiredAt(actionExpiredAt)
                .setOtpInvalidAttemptsLeft(otpMaxInvalidAttempts)
                .setOtpCountLeft(otpMaxNewGenerationPerAction);
        profileDeleteActionEntity.setOtpAction(otp);

        if (!otpCreateNewAction.isCredentialsValid()) {
            profileDeleteActionEntity.decrementPwdInvalidAttemptsLeft();
            getRepository().save(profileDeleteActionEntity);
            throw new BadCredentialsException("Bad credentials!");
        }
        return updateNewOtpAndSendToClient(profileDeleteActionEntity);
    }

    @Override
    protected String getActionName() {
        return "profile-delete-action";
    }

    @Override
    protected ActionRepository<ProfileDeleteActionEntity> getRepository() {
        return profileDeleteActionRepository;
    }

    @Override
    protected void sendNewOtpToClient(ProfileDeleteActionEntity action) {
        String login = action.getLogin();
        String otpValue = action.getOtpAction().getOtpValue();
        notificationProvider.sendProfileDeleteCode(login, otpValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ProfileDeleteActionResult buildActionInfo(ProfileDeleteActionEntity action) {
        return new ProfileDeleteActionResult(action);
    }
}
