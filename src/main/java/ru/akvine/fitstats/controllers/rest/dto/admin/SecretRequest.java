package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@ToString(exclude = "secret")
public class SecretRequest {
    @NotBlank
    private String secret;
}
