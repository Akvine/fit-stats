package ru.akvine.fitstats.controllers.rest.dto.security.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthFinishRequest extends LoginRequest {
    @NotBlank
    @ToString.Exclude
    private String otp;
}
