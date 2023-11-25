package ru.akvine.fitstats.services.dto.profile.change_email;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Accessors(chain = true)
public class ProfileChangeEmailActionRequest {
    private String newEmail;
    private String clientUuid;
    private String sessionId;
    private String password;
    @Nullable
    private String otp;
}
