package ru.akvine.fitstats.services.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Data
@Accessors(chain = true)
public class Filter {
    @Nullable
    private String filterName;
}
