package ru.akvine.fitstats.controllers.rest.dto.security.registration;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.OtpActionResponse;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class RegistrationResponse extends SuccessfulResponse {
    private String state;

    private OtpActionResponse otp;
}
