package ru.akvine.fitstats.services.processors.statistic.additional;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PercentStatisticProcessor {
    private static final int DEFAULT_PERCENT_VALUE = 100;

    public double calculate(List<Double> macronutrientValues, double totalCalories) {
        return macronutrientValues
                .stream()
                .mapToDouble(value -> value).sum() / totalCalories * DEFAULT_PERCENT_VALUE;
    }
}
