package ru.akvine.fitstats.services.processors.statistic.main;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MaxStatisticProcessor implements StatisticProcessor {
    @Override
    public double calculate(List<Double> values) {
        return values
                .stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0);
    }

    @Override
    public String getType() {
        return "max";
    }
}
