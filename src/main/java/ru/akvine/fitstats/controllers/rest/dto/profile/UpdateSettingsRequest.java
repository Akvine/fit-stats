package ru.akvine.fitstats.controllers.rest.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateSettingsRequest {
    private String language;
    private String telegramPrintMacronutrientsMode;
    private Integer roundAccuracy;
}
