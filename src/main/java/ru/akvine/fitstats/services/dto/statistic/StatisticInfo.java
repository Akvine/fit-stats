package ru.akvine.fitstats.services.dto.statistic;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class StatisticInfo {
    private Map<String, Map<String, Double>> statisticInfo;
}
