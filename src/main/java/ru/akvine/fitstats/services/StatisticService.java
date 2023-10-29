package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.repositories.DietRecordRepository;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.statistic.Statistic;
import ru.akvine.fitstats.services.dto.statistic.StatisticInfo;
import ru.akvine.fitstats.services.processors.macronutrient.MacronutrientProcessor;
import ru.akvine.fitstats.services.processors.statistic.StatisticProcessor;
import ru.akvine.fitstats.utils.MathUtils;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static ru.akvine.fitstats.utils.DateUtils.*;

@Service
@Slf4j
public class StatisticService {
    private final DietRecordRepository dietRecordRepository;
    private final ClientService clientService;
    private Map<String, StatisticProcessor> availableStatisticProcessors;
    private Map<String, MacronutrientProcessor> availableMacronutrientProcessors;

    @Autowired
    public StatisticService(List<StatisticProcessor> statisticProcessors,
                            List<MacronutrientProcessor> macronutrientProcessors,
                            ClientService clientService,
                            DietRecordRepository dietRecordRepository) {
        this.dietRecordRepository = dietRecordRepository;
        this.clientService = clientService;
        this.availableStatisticProcessors =
                statisticProcessors
                        .stream()
                        .collect(toMap(StatisticProcessor::getType, identity()));
        this.availableMacronutrientProcessors =
                macronutrientProcessors
                        .stream()
                        .collect(toMap(MacronutrientProcessor::getType, identity()));
    }

    public StatisticInfo calculateStatisticInfo(Statistic statistic) {
        Preconditions.checkNotNull(statistic, "statistic is null");
        String uuid = statistic.getClientUuid();
        Integer roundAccuracy = statistic.getRoundAccuracy();
        clientService.verifyExistsByUuidAndGet(uuid);

        DateRange findDateRange = getDateRange(statistic);
        List<DietRecordEntity> records = dietRecordRepository.findByDateRange(
                uuid,
                findDateRange.getStartDate(),
                findDateRange.getEndDate());
        Map<String, Map<String, Double>> indicatorStatistics = new LinkedHashMap<>();

        statistic
                .getIndicatorsWithMacronutrients()
                .entrySet()
                .stream()
                .forEach(entry -> {
                    String indicator = entry.getKey();
                    List<String> macronutrients = entry.getValue();
                    Map<String, Double> statsInfo = new LinkedHashMap<>();
                    macronutrients
                            .stream()
                            .forEach(macronutrient -> {
                                List<Double> macronutrientsValues = availableMacronutrientProcessors
                                        .get(macronutrient)
                                        .extract(records);
                                double statisticValue = availableStatisticProcessors
                                        .get(indicator)
                                        .calculate(macronutrientsValues);
                                statsInfo.put(macronutrient, MathUtils.round(statisticValue, roundAccuracy));
                            });
                    indicatorStatistics.put(indicator, statsInfo);
                });
        return new StatisticInfo()
                .setStatisticInfo(indicatorStatistics);
    }

    private DateRange getDateRange(Statistic statistic) {
        LocalDate startDate = statistic.getDateRange().getStartDate();
        LocalDate endDate = statistic.getDateRange().getEndDate();
        Duration duration = statistic.getDateRange().getDuration();

        DateRange findDateRange;

        if (statistic.getDateRange().getDuration() != null) {
            switch (duration) {
                case DAY:
                    findDateRange = getDayRange();
                    break;
                case WEEK:
                    findDateRange = getWeekRange();
                    break;
                case MONTH:
                    findDateRange = getMonthRange();
                    break;
                default:
                    findDateRange = getYearRange();
            }
        } else {
            findDateRange = new DateRange(startDate, endDate);
        }
        return findDateRange;
    }
}
