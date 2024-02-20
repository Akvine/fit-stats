package ru.akvine.fitstats.services.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;

@Data
@Accessors(chain = true)
public class UpdateSettings {
    private String email;
    private Language language;
    private Integer roundAccuracy;
    private PrintMacronutrientsMode printMacronutrientsMode;
}
