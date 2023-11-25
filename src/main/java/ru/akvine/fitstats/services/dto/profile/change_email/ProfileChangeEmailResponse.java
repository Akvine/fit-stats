package ru.akvine.fitstats.services.dto.profile.change_email;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.OtpActionResponse;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class ProfileChangeEmailResponse extends SuccessfulResponse {
    private int pwdInvalidAttemptsLeft;

    private OtpActionResponse otp;
}
