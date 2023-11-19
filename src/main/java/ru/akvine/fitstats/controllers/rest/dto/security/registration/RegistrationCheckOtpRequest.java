package ru.akvine.fitstats.controllers.rest.dto.security.registration;

import lombok.Data;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;

import javax.validation.constraints.NotBlank;

@Data
public class RegistrationCheckOtpRequest extends LoginRequest {
    @NotBlank
    private String otp;
}
