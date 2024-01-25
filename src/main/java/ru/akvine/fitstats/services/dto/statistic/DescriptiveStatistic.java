package ru.akvine.fitstats.services.dto.statistic;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.akvine.fitstats.enums.Macronutrient;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
public class DescriptiveStatistic extends Statistic {
    private Map<String, List<Macronutrient>> indicatorsWithMacronutrients;
}
