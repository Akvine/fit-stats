package ru.akvine.fitstats.controllers.telegram.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
public class TelegramProfileUpdateSettingsRequest extends TelegramBaseRequest {
    private String roundAccuracy;
    private String language;
    private String printMode;
}
