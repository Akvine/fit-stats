package ru.akvine.fitstats.services.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.Macronutrient;

@Data
@Accessors(chain = true)
public class StatisticHistory {
    private String clientUuid;
    private Macronutrient macronutrient;
    private Duration duration;
}
