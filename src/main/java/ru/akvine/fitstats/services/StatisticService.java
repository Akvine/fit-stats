package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.repositories.DietRecordRepository;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.statistic.*;
import ru.akvine.fitstats.services.processors.macronutrient.MacronutrientProcessor;
import ru.akvine.fitstats.services.processors.statistic.additional.ModeStatisticProcessor;
import ru.akvine.fitstats.services.processors.statistic.main.StatisticProcessor;
import ru.akvine.fitstats.utils.MathUtils;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static ru.akvine.fitstats.utils.DateUtils.*;

@Service
@Slf4j
public class StatisticService {
    private final DietRecordRepository dietRecordRepository;
    private final ClientService clientService;
    private final ModeStatisticProcessor modeStatisticProcessor;
    private Map<String, StatisticProcessor> availableStatisticProcessors;
    private Map<String, MacronutrientProcessor> availableMacronutrientProcessors;

    @Autowired
    public StatisticService(List<StatisticProcessor> statisticProcessors,
                            List<MacronutrientProcessor> macronutrientProcessors,
                            ClientService clientService,
                            DietRecordRepository dietRecordRepository,
                            ModeStatisticProcessor modeStatisticProcessor) {
        this.dietRecordRepository = dietRecordRepository;
        this.clientService = clientService;
        this.modeStatisticProcessor = modeStatisticProcessor;
        this.availableStatisticProcessors =
                statisticProcessors
                        .stream()
                        .collect(toMap(StatisticProcessor::getType, identity()));
        this.availableMacronutrientProcessors =
                macronutrientProcessors
                        .stream()
                        .collect(toMap(MacronutrientProcessor::getType, identity()));
    }

    public MainStatisticInfo calculateMainStatisticInfo(MainStatistic mainStatistic) {
        Preconditions.checkNotNull(mainStatistic, "statistic is null");
        String uuid = mainStatistic.getClientUuid();
        Integer roundAccuracy = mainStatistic.getRoundAccuracy();
        clientService.verifyExistsByUuidAndGet(uuid);

        DateRange findDateRange = getDateRange(mainStatistic);
        List<DietRecordEntity> records = dietRecordRepository.findByDateRange(
                uuid,
                findDateRange.getStartDate(),
                findDateRange.getEndDate());
        Map<String, Map<String, Double>> indicatorStatistics = new LinkedHashMap<>();

        mainStatistic
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
        return new MainStatisticInfo()
                .setStatisticInfo(indicatorStatistics);
    }

    public AdditionalStatisticInfo calculateAdditionalStatisticInfo(AdditionalStatistic additionalStatistic) {
        Preconditions.checkNotNull(additionalStatistic, "additionalStatistic is null");

        String uuid = additionalStatistic.getClientUuid();
        Integer roundAccuracy = additionalStatistic.getRoundAccuracy();
        int modeCount = additionalStatistic.getModeCount();
        clientService.verifyExistsByUuidAndGet(uuid);

        AdditionalStatisticInfo additionalStatisticInfo = new AdditionalStatisticInfo();
        DateRange findDateRange = getDateRange(additionalStatistic);
        List<DietRecordEntity> records = dietRecordRepository.findByDateRange(
                uuid,
                findDateRange.getStartDate(),
                findDateRange.getEndDate());
        List<ProductBean> products = records
                .stream()
                .map(record -> new ProductBean(record.getProduct()))
                .collect(Collectors.toList());
        return additionalStatisticInfo
                .setProductCount(modeStatisticProcessor.calculate(products, modeCount));
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
