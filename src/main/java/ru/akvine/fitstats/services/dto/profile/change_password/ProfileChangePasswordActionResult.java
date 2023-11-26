package ru.akvine.fitstats.services.dto.profile.change_password;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.profile.ProfileChangePasswordActionEntity;
import ru.akvine.fitstats.services.dto.security.OtpAction;

@Data
@Accessors(chain = true)
public class ProfileChangePasswordActionResult {
    private int pwdInvalidAttemptsLeft;
    private OtpAction otp;

    public ProfileChangePasswordActionResult(ProfileChangePasswordActionEntity profileChangePasswordActionEntity) {
        this.pwdInvalidAttemptsLeft = profileChangePasswordActionEntity.getPwdInvalidAttemptsLeft();
        this.otp = new OtpAction(profileChangePasswordActionEntity.getOtpAction());
    }
}
