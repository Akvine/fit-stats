package ru.akvine.fitstats.managers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.StatisticType;
import ru.akvine.fitstats.services.processors.statistic.main.StatisticProcessor;

import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class StatisticProcessorsManager {
    private Map<StatisticType, StatisticProcessor> statisticProcessors;
}
