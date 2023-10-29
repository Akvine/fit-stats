package ru.akvine.fitstats.services.dto.statistic;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.services.dto.DateRange;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class Statistic {
    private String clientUuid;
    private Map<String, List<String>> indicatorsWithMacronutrients;
    @Nullable
    private Integer roundAccuracy;
    private DateRange dateRange;
}
