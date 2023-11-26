package ru.akvine.fitstats.services.dto.profile.change_password;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Accessors(chain = true)
public class ProfileChangePasswordActionRequest {
    private String newPassword;
    private String currentPassword;
    private String sessionId;
    private String clientUuid;
    @Nullable
    private String otp;
}
