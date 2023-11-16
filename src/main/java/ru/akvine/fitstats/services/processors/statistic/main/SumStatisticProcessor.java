package ru.akvine.fitstats.services.processors.statistic.main;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SumStatisticProcessor implements StatisticProcessor {
    @Override
    public double calculate(List<Double> values) {
        return values
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    @Override
    public String getType() {
        return "sum";
    }
}
