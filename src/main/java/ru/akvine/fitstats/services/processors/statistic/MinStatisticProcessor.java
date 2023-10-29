package ru.akvine.fitstats.services.processors.statistic;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MinStatisticProcessor implements StatisticProcessor {
    @Override
    public double calculate(List<Double> values) {
        return values
                .stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0);
    }

    @Override
    public String getType() {
        return "min";
    }
}
