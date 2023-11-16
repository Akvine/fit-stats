package ru.akvine.fitstats.services.dto.statistic;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
public class MainStatistic extends Statistic {
    private Map<String, List<String>> indicatorsWithMacronutrients;
}
