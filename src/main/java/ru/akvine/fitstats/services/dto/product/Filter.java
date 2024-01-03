package ru.akvine.fitstats.services.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.MacronutrientFilterPart;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Filter {
    @Nullable
    private String filterName;
    @Nullable
    private List<MacronutrientFilterPart> macronutrientFilterParts;
}
