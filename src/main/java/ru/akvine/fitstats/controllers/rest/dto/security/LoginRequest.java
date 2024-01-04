package ru.akvine.fitstats.controllers.rest.dto.security;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public abstract class LoginRequest {
    @NotBlank
    private String login;
}
