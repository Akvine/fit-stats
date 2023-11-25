package ru.akvine.fitstats.controllers.rest.validators.security;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreFinishRequest;
import ru.akvine.fitstats.exceptions.client.ClientNotFoundException;
import ru.akvine.fitstats.services.ClientService;
import ru.akvine.fitstats.validators.EmailValidator;
import ru.akvine.fitstats.validators.PasswordValidator;

@Component
@RequiredArgsConstructor
public class AccessRestoreValidator {
    private final ClientService clientService;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;

    public void verifyAccessRestore(LoginRequest request) {
        Preconditions.checkNotNull(request, "loginRequest is null");
        Preconditions.checkNotNull(request.getLogin(), "loginRequest.login is null");

        String email = request.getLogin();
        emailValidator.validate(email);
        verifyNotExistsByLogin(email);
    }

    public void verifyAccessRestoreFinish(AccessRestoreFinishRequest request) {
        Preconditions.checkNotNull(request, "accessRestoreFinishRequest is null");
        Preconditions.checkNotNull(request.getLogin(), "accessRestoreFinishRequest.login is null");
        Preconditions.checkNotNull(request.getPassword(), "accessRestoreFinishRequest.password is null");

        verifyNotExistsByLogin(request.getLogin());
        passwordValidator.validate(request.getPassword());
    }

    public void verifyNotExistsByLogin(String login) {
        boolean exists = clientService.isExistsByEmail(login);
        if (!exists) {
            throw new ClientNotFoundException("Client with email = [" + login + "] not registered!");
        }
    }
}
