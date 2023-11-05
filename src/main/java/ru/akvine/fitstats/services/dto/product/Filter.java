package ru.akvine.fitstats.services.dto.product;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class Filter {
    @Nullable
    private String filterName;
    @Nullable
    private Map<String, Double> macronutrientsWithValues;
}
