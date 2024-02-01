package ru.akvine.fitstats.services.profile;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.services.security.PasswordRequiredActionService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileDeleteActionService extends PasswordRequiredActionService<ProfileDeleteActionEntity> {
    private final ProfileDeleteActionRepository profileDeleteActionRepository;
    private final PropertyParseService propertyParseService;

    @Value("security.otp.action.lifetime.seconds")
    private String otpActionLifetimeSeconds;
    @Value("security.otp.max.invalid.attempts")
    private String otpMaxInvalidAttempts;
    @Value("security.otp.max.new.generation.per.action")
    private String otpMaxNewGenerationPerAction;

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
            logger.info("Client with email = {} tried to {}, but action is not started", login, getActionName());
            throw new ActionNotStartedException(String.format("Can't finish %s, action not initiated!", getActionName()));
        }

        // Действие просрочено
        if (deleteActionEntity.getOtpAction().isActionExpired()) {
            logger.info("Client with email = {} tried to {}, but action is expired", login, getActionName());
            getRepository().delete(deleteActionEntity);
            logger.info("Expired action = {}[id = {}] removed from DB", getActionName(), deleteActionEntity.getId());
            throw new ActionNotStartedException(String.format("Can't finish %s, action is expired!", getActionName()));
        }

        // Действие не просрочено, но просрочен код
        if (deleteActionEntity.getOtpAction().isExpiredOtp()) {
            logger.info("Client with email = {} tried to finish = {}, but otp is expired! New otp left = {}", login, getActionName(), deleteActionEntity.getOtpAction().getOtpCountLeft());
            throw new OtpExpiredException(deleteActionEntity.getOtpAction().getOtpCountLeft());
        }

        // Действие не просрочено и код еще активен - проверяем
        if (deleteActionEntity.getOtpAction().isOtpValid(otp)) {
            clientService.delete(login);
            logger.info("Client with email = {} successfully passed one-time-password and was deleted", login);

            getRepository().delete(deleteActionEntity);
            return;
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
        LocalDateTime actionExpiredAt = now.plusSeconds(propertyParseService.parseInteger(otpActionLifetimeSeconds));

        ProfileDeleteActionEntity profileDeleteActionEntity = new ProfileDeleteActionEntity()
                .setLogin(otpCreateNewAction.getLogin())
                .setSessionId(otpCreateNewAction.getSessionId())
                .setPwdInvalidAttemptsLeft(propertyParseService.parseInteger(otpMaxInvalidAttempts));

        OtpActionEntity otp = new OtpActionEntity()
                .setStartedDate(now)
                .setActionExpiredAt(actionExpiredAt)
                .setOtpInvalidAttemptsLeft(propertyParseService.parseInteger(otpMaxInvalidAttempts))
                .setOtpCountLeft(propertyParseService.parseInteger(otpMaxNewGenerationPerAction));
        profileDeleteActionEntity.setOtpAction(otp);

        if (!otpCreateNewAction.isCredentialsValid()) {
            profileDeleteActionEntity.decrementPwdInvalidAttemptsLeft();
            getRepository().save(profileDeleteActionEntity);
            logger.info("Client with email = {} tried to initiate {}, but entered wrong account password! Invalid attempts left = {}",
                    profileDeleteActionEntity.getLogin(), getActionName(), profileDeleteActionEntity.getPwdInvalidAttemptsLeft());
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
