package ru.akvine.fitstats.controllers.rest.dto.profile.change_email;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class ProfileChangeEmailStartRequest {
    @NotBlank
    private String newEmail;
}
