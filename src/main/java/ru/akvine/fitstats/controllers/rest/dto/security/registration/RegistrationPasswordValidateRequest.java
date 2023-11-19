package ru.akvine.fitstats.controllers.rest.dto.security.registration;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegistrationPasswordValidateRequest {
    @NotBlank
    private String password;
}
