package ru.akvine.fitstats.services.processors.statistic.main;

import ru.akvine.fitstats.enums.StatisticType;

import java.util.List;

public interface StatisticProcessor {
    double calculate(List<Double> values);
    StatisticType getType();
}
