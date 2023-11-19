package ru.akvine.fitstats.controllers.rest.dto.security;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public abstract class LoginRequest {
    @NotBlank
    private String login;
}
