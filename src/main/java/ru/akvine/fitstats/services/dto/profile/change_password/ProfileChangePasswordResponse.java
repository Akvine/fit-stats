package ru.akvine.fitstats.services.dto.profile.change_password;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.OtpActionResponse;

@Data
@Accessors(chain = true)
public class ProfileChangePasswordResponse extends SuccessfulResponse {
    private int pwdInvalidAttemptsLeft;

    private OtpActionResponse otp;
}
