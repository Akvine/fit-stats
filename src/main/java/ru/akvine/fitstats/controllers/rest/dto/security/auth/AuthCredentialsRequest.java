package ru.akvine.fitstats.controllers.rest.dto.security.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthCredentialsRequest extends LoginRequest {
    @NotBlank
    private String password;
}
