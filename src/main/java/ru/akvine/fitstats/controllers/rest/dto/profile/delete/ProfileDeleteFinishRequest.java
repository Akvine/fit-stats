package ru.akvine.fitstats.controllers.rest.dto.profile.delete;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class ProfileDeleteFinishRequest {
    @NotBlank
    private String otp;
}
