package ru.akvine.fitstats.services.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class AdditionalStatisticInfo {
    private Map<String, Integer> mode;
    private Map<String, Double> macronutrientsPercent;
}
