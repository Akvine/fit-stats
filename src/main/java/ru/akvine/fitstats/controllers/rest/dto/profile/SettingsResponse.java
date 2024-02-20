package ru.akvine.fitstats.controllers.rest.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class SettingsResponse extends SuccessfulResponse {
    private String telegramPrintMacronutrientsMode;
    private Integer roundAccuracy;
    private String language;
}
