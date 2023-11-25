package ru.akvine.fitstats.services.dto.profile.delete;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.profile.ProfileDeleteActionEntity;
import ru.akvine.fitstats.services.dto.security.OtpAction;

@Data
@Accessors(chain = true)
public class ProfileDeleteActionResult {
    private int pwdInvalidAttemptsLeft;
    private OtpAction otp;

    public ProfileDeleteActionResult(ProfileDeleteActionEntity profileDeleteActionEntity) {
        this.pwdInvalidAttemptsLeft = profileDeleteActionEntity.getPwdInvalidAttemptsLeft();
        this.otp = new OtpAction(profileDeleteActionEntity.getOtpAction());
    }
}
