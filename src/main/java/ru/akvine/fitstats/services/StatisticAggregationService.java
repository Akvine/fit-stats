package ru.akvine.fitstats.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.services.dto.statistic.DietStatisticHistory;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Service
@Slf4j
public class StatisticAggregationService {
    public Map<String, DietStatisticHistory> calculatePastDays(List<DietRecordEntity> entities, int daysCount) {
        int accuracy = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getRoundAccuracy();
        return entities.stream()
                .filter(entity -> entity.getDate().isAfter(LocalDate.now().minusDays(daysCount)))
                .collect(Collectors.groupingBy(
                        entity -> entity.getDate().toString(),
                        LinkedHashMap::new,
                        Collectors.reducing(
                                new DietStatisticHistory(),
                                entity -> new DietStatisticHistory(
                                        entity.getDate().toString(),
                                        entity.getProteins(),
                                        entity.getFats(),
                                        entity.getCarbohydrates(),
                                        entity.getAlcohol(),
                                        entity.getCalories()
                                ),
                                (agg1, agg2) -> new DietStatisticHistory(
                                        agg1.getDate(),
                                        round(agg1.getProteins() + agg2.getProteins(), accuracy),
                                        round(agg1.getFats() + agg2.getFats(), accuracy),
                                        round(agg1.getCarbohydrates() + agg2.getCarbohydrates(), accuracy),
                                        round(agg1.getAlcohol() + agg2.getAlcohol(), accuracy),
                                        round(agg1.getCalories() + agg2.getCalories(), accuracy)
                                )
                        )
                ));
    }

    public  Map<String, DietStatisticHistory> calculateWeeksPerHalfYear(List<DietRecordEntity> entities) {
        int monthCount = 6;
        int accuracy = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getRoundAccuracy();
        return entities.stream()
                .filter(entity -> entity.getDate().isAfter(LocalDate.now().minusMonths(monthCount)))
                .collect(Collectors.groupingBy(
                        entity -> String.valueOf(entity.getDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)),
                        Collectors.reducing(
                                new DietStatisticHistory(),
                                entity -> new DietStatisticHistory(
                                        String.valueOf(entity.getDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)),
                                        entity.getProteins(),
                                        entity.getFats(),
                                        entity.getCarbohydrates(),
                                        entity.getAlcohol(),
                                        entity.getCalories()
                                ),
                                (agg1, agg2) -> new DietStatisticHistory(
                                        agg1.getDate(),
                                        round(agg1.getProteins() + agg2.getProteins(), accuracy),
                                        round(agg1.getFats() + agg2.getFats(), accuracy),
                                        round(agg1.getCarbohydrates() + agg2.getCarbohydrates(), accuracy),
                                        round(agg1.getAlcohol() + agg2.getAlcohol(), 2),
                                        round(agg1.getCalories() + agg2.getCalories(), accuracy)
                                )
                        )
                ));
    }

    public Map<String, DietStatisticHistory> calculateMonths(List<DietRecordEntity> entities) {
        int yearCount = 1;
        int accuracy = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getRoundAccuracy();
        return entities.stream()
                .filter(entity -> entity.getDate().isAfter(LocalDate.now().minusYears(yearCount)))
                .collect(Collectors.groupingBy(
                        entity -> String.valueOf(entity.getDate().getMonthValue()),
                        Collectors.reducing(
                                new DietStatisticHistory(),
                                entity -> new DietStatisticHistory(
                                        String.valueOf(entity.getDate().getMonthValue()),
                                        entity.getProteins(),
                                        entity.getFats(),
                                        entity.getCarbohydrates(),
                                        entity.getAlcohol(),
                                        entity.getCalories()
                                ),
                                (agg1, agg2) -> new DietStatisticHistory(
                                        agg1.getDate(),
                                        round(agg1.getProteins() + agg2.getProteins(), accuracy),
                                        round(agg1.getFats() + agg2.getFats(), accuracy),
                                        round(agg1.getCarbohydrates() + agg2.getCarbohydrates(), accuracy),
                                        round(agg1.getAlcohol() + agg2.getAlcohol(), accuracy),
                                        round(agg1.getCalories() + agg2.getCalories(), accuracy)
                                )
                        )
                ));
    }

    public Map<String, DietStatisticHistory> calculateYears(List<DietRecordEntity> entities, int yearCount) {
        int accuracy = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getRoundAccuracy();
        return entities.stream()
                .filter(entity -> entity.getDate().isAfter(LocalDate.now().minusYears(yearCount)))
                .collect(Collectors.groupingBy(
                        entity -> String.valueOf(entity.getDate().getYear()),
                        Collectors.reducing(
                                new DietStatisticHistory(),
                                entity -> new DietStatisticHistory(
                                        String.valueOf(entity.getDate().getYear()),
                                        entity.getProteins(),
                                        entity.getFats(),
                                        entity.getCarbohydrates(),
                                        entity.getAlcohol(),
                                        entity.getCalories()
                                ),
                                (agg1, agg2) -> new DietStatisticHistory(
                                        agg1.getDate(),
                                        round(agg1.getProteins() + agg2.getProteins(), accuracy),
                                        round(agg1.getFats() + agg2.getFats(), accuracy),
                                        round(agg1.getCarbohydrates() + agg2.getCarbohydrates(), accuracy),
                                        round(agg1.getAlcohol() + agg2.getAlcohol(), accuracy),
                                        round(agg1.getCalories() + agg2.getCalories(), accuracy)
                                )
                        )
                ));
    }
}
