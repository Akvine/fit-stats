package ru.akvine.fitstats.services.processors.statistic.main;

import java.util.List;

public interface StatisticProcessor {
    double calculate(List<Double> values);
    String getType();
}
