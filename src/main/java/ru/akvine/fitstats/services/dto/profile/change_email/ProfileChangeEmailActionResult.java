package ru.akvine.fitstats.services.dto.profile.change_email;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.profile.ProfileChangeEmailActionEntity;
import ru.akvine.fitstats.services.dto.security.OtpAction;

@Data
@Accessors(chain = true)
public class ProfileChangeEmailActionResult {
    private int pwdInvalidAttemptsLeft;
    private OtpAction otp;

    public ProfileChangeEmailActionResult(ProfileChangeEmailActionEntity profileChangeEmailActionEntity) {
        this.pwdInvalidAttemptsLeft = profileChangeEmailActionEntity.getPwdInvalidAttemptsLeft();
        this.otp = new OtpAction(profileChangeEmailActionEntity.getOtpAction());
    }
}
