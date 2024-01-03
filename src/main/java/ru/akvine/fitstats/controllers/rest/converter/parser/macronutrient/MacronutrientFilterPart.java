package ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class MacronutrientFilterPart {
    private String macronutrient;
    private String condition;
    private String value;
    @Nullable
    private String logicCondition;
}
