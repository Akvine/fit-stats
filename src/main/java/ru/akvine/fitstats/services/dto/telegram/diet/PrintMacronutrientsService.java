package ru.akvine.fitstats.services.dto.telegram.diet;

import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.dto.diet.DietDisplay;

public interface PrintMacronutrientsService {
    String NEXT_LINE = "\n";

    String print(DietDisplay dietDisplay, ClientSettingsBean clientSettingsBean);

    PrintMacronutrientsMode getType();
}
