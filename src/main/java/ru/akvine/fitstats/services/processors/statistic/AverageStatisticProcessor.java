package ru.akvine.fitstats.services.processors.statistic;

import org.springframework.stereotype.Component;

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
    public String getType() {
        return "avg";
    }
}
