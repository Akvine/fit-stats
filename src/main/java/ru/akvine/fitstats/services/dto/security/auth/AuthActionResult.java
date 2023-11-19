package ru.akvine.fitstats.services.dto.security.auth;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.security.AuthActionEntity;
import ru.akvine.fitstats.services.dto.security.OtpAction;

@Data
@Accessors(chain = true)
public class AuthActionResult {
    private OtpAction otp;

    public AuthActionResult(AuthActionEntity authActionEntity) {
        this.otp = new OtpAction(authActionEntity.getOtpAction());
    }
}
