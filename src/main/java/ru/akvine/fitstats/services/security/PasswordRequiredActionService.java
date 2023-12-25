package ru.akvine.fitstats.services.security;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.security.AccountPasswordable;
import ru.akvine.fitstats.entities.security.OneTimePasswordable;
import ru.akvine.fitstats.entities.security.OtpInfo;
import ru.akvine.fitstats.services.ClientService;
import ru.akvine.fitstats.services.PasswordService;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.security.OtpCreateNewAction;

@Service
@Slf4j
public abstract class PasswordRequiredActionService <T extends AccountPasswordable & OneTimePasswordable> extends OtpActionService<T> {
    @Autowired
    protected ClientService clientService;
    @Autowired
    protected PasswordService passwordService;

    protected boolean isValidPassword(ClientBean clientBean, String password) {
        Preconditions.checkNotNull(clientBean, "clientBean is null");
        Preconditions.checkNotNull(password, "password is null");
        return passwordService.isValidPassword(clientBean, password);
    }

    protected void handleInvalidPasswordInput(T action) {
        action.decrementPwdInvalidAttemptsLeft();
        if (action.getPwdInvalidAttemptsLeft() <= 0) {
            handleNoMorePasswordInvalidAttemptsLeft(action);
            throw new BadCredentialsException("Client reached limit of password invalid attempts");
        } else {
            action = getRepository().save(action);
            logger.info("Client with email = {} tried to initiate {}, but entered wrong account password. Invalid attempts left = {}",
                    action.getLogin(), getActionName(), action.getPwdInvalidAttemptsLeft());
            throw new BadCredentialsException("Invalid password");
        }
    }

    protected void handleNoMorePasswordInvalidAttemptsLeft(T action) {
        String login = action.getLogin();
        blockingService.setBlock(login);
        logger.info("Client with email = {} reached limit for invalid password input and set blocked", login);
        getRepository().delete(action);
        logger.info("Blocked client's with email = {} {}[id={}] removed from DB", login, getActionName(), action.getId());
    }

    protected abstract T createNewActionAndSendOtp(OtpCreateNewAction otpCreateNewAction);

    protected T updateNewOtpAndSend(T action) {
        String login = action.getLogin();
        OtpInfo newOtpInfo = otpService.getOneTimePassword(login);

        action.getOtpAction().setNewOtpValue(newOtpInfo);
        sendNewOtpToClient(action);
        return getRepository().save(action);
    }
}
