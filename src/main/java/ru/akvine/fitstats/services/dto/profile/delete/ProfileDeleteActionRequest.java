package ru.akvine.fitstats.services.dto.profile.delete;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Accessors(chain = true)
public class ProfileDeleteActionRequest {
    private String sessionId;
    private String clientUuid;
    @Nullable
    private String otp;
}
