package ru.akvine.fitstats.managers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.Macronutrient;
import ru.akvine.fitstats.services.processors.macronutrient.MacronutrientProcessor;

import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class MacronutrientProcessorsManager {
    private Map<Macronutrient, MacronutrientProcessor> macronutrientProcessors;
}
