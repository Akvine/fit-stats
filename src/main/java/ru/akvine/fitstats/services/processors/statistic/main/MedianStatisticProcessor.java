package ru.akvine.fitstats.services.processors.statistic.main;

import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.StatisticType;

import java.util.Collections;
import java.util.List;

@Component
public class MedianStatisticProcessor implements StatisticProcessor {
    @Override
    public double calculate(List<Double> values) {
        Collections.sort(values);
        int size = values.size();

        if (values.isEmpty()) {
            return 0;
        }

        if (size % 2 == 0) {
            int middleIndex1 = size / 2 - 1;
            int middleIndex2 = size / 2;
            double middleValue1 = values.get(middleIndex1);
            double middleValue2 = values.get(middleIndex2);
            return (middleValue1 + middleValue2) / 2;
        }
        int middleIndex = size / 2;
        return values.get(middleIndex);
    }

    @Override
    public StatisticType getType() {
        return StatisticType.MEDIAN;
    }
}
