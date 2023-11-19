package ru.akvine.fitstats.services.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.Macronutrient;

import java.util.Map;

@Data
@Accessors(chain = true)
public class StatisticHistoryResult {
    private Duration duration;
    private Macronutrient macronutrient;
    private double average;
    private Map<String, Double> history;
}
