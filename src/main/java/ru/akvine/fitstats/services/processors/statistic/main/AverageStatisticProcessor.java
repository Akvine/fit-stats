package ru.akvine.fitstats.services.processors.statistic.main;

import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.StatisticType;

import java.util.List;

@Component
public class AverageStatisticProcessor implements StatisticProcessor {
    @Override
    public double calculate(List<Double> values) {
        return values
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum() / values.size();
    }

    @Override
    public StatisticType getType() {
        return StatisticType.AVG;
    }
}
