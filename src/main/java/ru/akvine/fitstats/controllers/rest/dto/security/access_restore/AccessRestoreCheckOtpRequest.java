package ru.akvine.fitstats.controllers.rest.dto.security.access_restore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccessRestoreCheckOtpRequest extends LoginRequest {
    @NotBlank
    private String otp;
}
