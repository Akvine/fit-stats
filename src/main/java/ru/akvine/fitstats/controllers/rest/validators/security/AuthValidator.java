package ru.akvine.fitstats.controllers.rest.validators.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;
import ru.akvine.fitstats.validators.EmailValidator;

@Component
@RequiredArgsConstructor
public class AuthValidator {
    private final EmailValidator emailValidator;

    public void verifyAuthLogin(LoginRequest request) {
        emailValidator.validate(request.getLogin());
    }
}
