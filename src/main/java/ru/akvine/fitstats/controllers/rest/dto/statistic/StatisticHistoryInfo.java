package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class StatisticHistoryInfo {
    private String duration;
    private String macronutrient;
    private Map<String, Double> history;
    private double average;
    private double median;
}
