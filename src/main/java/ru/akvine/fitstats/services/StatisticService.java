package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.Macronutrient;
import ru.akvine.fitstats.enums.StatisticType;
import ru.akvine.fitstats.managers.MacronutrientProcessorsManager;
import ru.akvine.fitstats.managers.StatisticProcessorsManager;
import ru.akvine.fitstats.repositories.DietRecordRepository;
import ru.akvine.fitstats.services.client.ClientService;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.statistic.*;
import ru.akvine.fitstats.services.processors.statistic.additional.ModeStatisticProcessor;
import ru.akvine.fitstats.services.processors.statistic.additional.PercentStatisticProcessor;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.akvine.fitstats.constants.MacronutrientsConstants.*;
import static ru.akvine.fitstats.utils.DateUtils.*;
import static ru.akvine.fitstats.utils.MathUtils.round;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticService {
    private static final String FATS_MACRONUTRIENT_NAME = "fats";
    private static final String PROTEINS_MACRONUTRIENT_NAME = "proteins";
    private static final String CARBOHYDRATES_MACRONUTRIENT_NAME = "carbohydrates";
    private static final String ALCOHOL_NAME = "alcohol";

    private static final int MONTH_AVERAGE_COUNT = 30;
    private static final int YEARS_COUNT = 5;

    private final DietRecordRepository dietRecordRepository;
    private final ClientService clientService;
    private final ModeStatisticProcessor modeStatisticProcessor;
    private final PercentStatisticProcessor percentStatisticProcessor;
    private final StatisticProcessorsManager statisticProcessorsManager;
    private final MacronutrientProcessorsManager macronutrientProcessorsManager;
    private final StatisticAggregationService statisticAggregationService;

    public DescriptiveStatisticInfo calculateDescriptiveStatisticInfo(DescriptiveStatistic descriptiveStatistic) {
        Preconditions.checkNotNull(descriptiveStatistic, "descriptiveStatistic is null");
        String uuid = descriptiveStatistic.getClientUuid();
        int roundAccuracy = descriptiveStatistic.getRoundAccuracy();
        clientService.verifyExistsByUuidAndGet(uuid);

        DateRange findDateRange = getDateRange(descriptiveStatistic);
        List<DietRecordEntity> records = dietRecordRepository.findByDateRange(
                uuid,
                findDateRange.getStartDate(),
                findDateRange.getEndDate());
        Map<String, Map<String, Double>> indicatorStatistics = new LinkedHashMap<>();

        descriptiveStatistic
                .getIndicatorsWithMacronutrients()
                .forEach((indicator, macronutrients) -> {
                    Map<String, Double> statsInfo = new LinkedHashMap<>();
                    macronutrients
                            .forEach(macronutrient -> {
                                List<Double> macronutrientsValues = macronutrientProcessorsManager.getMacronutrientProcessors()
                                        .get(macronutrient)
                                        .extract(records);
                                double statisticValue = statisticProcessorsManager.getStatisticProcessors()
                                        .get(indicator)
                                        .calculate(macronutrientsValues);
                                statsInfo.put(macronutrient.name().toLowerCase(), round(statisticValue, roundAccuracy));
                            });
                    indicatorStatistics.put(indicator.toString().toLowerCase(), statsInfo);
                });
        return new DescriptiveStatisticInfo()
                .setStatisticInfo(indicatorStatistics);
    }

    @Transactional
    public AdditionalStatisticInfo calculateAdditionalStatisticInfo(AdditionalStatistic additionalStatistic) {
        Preconditions.checkNotNull(additionalStatistic, "additionalStatistic is null");

        String uuid = additionalStatistic.getClientUuid();
        int roundAccuracy = additionalStatistic.getRoundAccuracy();
        int modeCount = additionalStatistic.getModeCount();
        clientService.verifyExistsByUuidAndGet(uuid);

        AdditionalStatisticInfo additionalStatisticInfo = new AdditionalStatisticInfo();
        Map<String, Double> macronutrientsPercents = new LinkedHashMap<>();
        DateRange findDateRange = getDateRange(additionalStatistic);
        List<DietRecordEntity> records = dietRecordRepository.findByDateRange(
                uuid,
                findDateRange.getStartDate(),
                findDateRange.getEndDate());
        List<ProductBean> products = records
                .stream()
                .map(record -> new ProductBean(record.getProduct()))
                .collect(Collectors.toList());

        List<Double> proteinsValues = macronutrientProcessorsManager.getMacronutrientProcessors()
                .get(Macronutrient.PROTEINS)
                .extract(records)
                .stream()
                .map(value -> value * PROTEINS_MACRONUTRIENT_CALORIES_COEFFICIENT)
                .collect(Collectors.toList());
        List<Double> fatsValues = macronutrientProcessorsManager.getMacronutrientProcessors()
                .get(Macronutrient.FATS)
                .extract(records)
                .stream()
                .map(value -> value * FATS_MACRONUTRIENT_CALORIES_COEFFICIENT)
                .collect(Collectors.toList());
        List<Double> carbohydrates = macronutrientProcessorsManager.getMacronutrientProcessors()
                .get(Macronutrient.CARBOHYDRATES)
                .extract(records)
                .stream()
                .map(value -> value * CARBOHYDRATES_MACRONUTRIENT_CALORIES_COEFFICIENT)
                .collect(Collectors.toList());
        List<Double> alcohol = macronutrientProcessorsManager.getMacronutrientProcessors()
                .get(Macronutrient.ALCOHOL)
                .extract(records)
                .stream()
                .map(value -> value * ALCOHOL_MACRONUTRIENT_CALORIES_COEFFICIENT)
                .collect(Collectors.toList());
        double totalCalories = macronutrientProcessorsManager.getMacronutrientProcessors()
                .get(Macronutrient.CALORIES)
                .extract(records)
                .stream()
                .mapToDouble(value -> value)
                .sum();

        double proteinsPercent = round(
                percentStatisticProcessor.calculate(proteinsValues, totalCalories), roundAccuracy);
        double fatsPercent = round(
                percentStatisticProcessor.calculate(fatsValues, totalCalories), roundAccuracy);
        double carbohydratesPercent = round(
                percentStatisticProcessor.calculate(carbohydrates, totalCalories), roundAccuracy);
        double alcoholPercent = round(percentStatisticProcessor.calculate(alcohol, totalCalories), roundAccuracy);

        macronutrientsPercents.put(PROTEINS_MACRONUTRIENT_NAME, proteinsPercent);
        macronutrientsPercents.put(FATS_MACRONUTRIENT_NAME, fatsPercent);
        macronutrientsPercents.put(CARBOHYDRATES_MACRONUTRIENT_NAME, carbohydratesPercent);
        macronutrientsPercents.put(ALCOHOL_NAME, alcoholPercent);

        return additionalStatisticInfo
                .setMode(modeStatisticProcessor.calculate(products, modeCount))
                .setMacronutrientsPercent(macronutrientsPercents);
    }

    public StatisticHistoryResult statisticHistoryInfo(StatisticHistory statisticHistory) {
        Preconditions.checkNotNull(statisticHistory, "statisticHistory is null");
        clientService.verifyExistsByUuidAndGet(statisticHistory.getClientUuid());

        Macronutrient macronutrient = statisticHistory.getMacronutrient();
        Duration duration = statisticHistory.getDuration();
        Map<String, DietStatisticHistory> statisticHistoryMap;

        Map<String, Double> macronutrientHistory;
        double average, median;

        List<DietRecordEntity> records = dietRecordRepository.findAll();
        switch (duration) {
            case DAY:
                statisticHistoryMap = statisticAggregationService.calculatePastDays(records, MONTH_AVERAGE_COUNT);
                break;
            case WEEK:
                statisticHistoryMap = statisticAggregationService.calculateWeeksPerHalfYear(records);
                break;
            case MONTH:
                statisticHistoryMap = statisticAggregationService.calculateMonths(records);
                break;
            case YEAR:
                statisticHistoryMap = statisticAggregationService.calculateYears(records, YEARS_COUNT);
                break;
            default:
                throw new IllegalArgumentException("Duration type = [" + duration + "] is not supported!");
        }

        switch (macronutrient) {
            case PROTEINS:
                macronutrientHistory = statisticHistoryMap
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, DietStatisticHistory>comparingByKey().reversed())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().getProteins(),
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        ));
                break;
            case FATS:
                macronutrientHistory = statisticHistoryMap
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, DietStatisticHistory>comparingByKey().reversed())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().getFats(),
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        ));
                break;
            case CARBOHYDRATES:
                macronutrientHistory = statisticHistoryMap
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, DietStatisticHistory>comparingByKey().reversed())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().getCarbohydrates(),
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        ));
                break;
            case ALCOHOL:
                macronutrientHistory = statisticHistoryMap
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, DietStatisticHistory>comparingByKey().reversed())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().getAlcohol(),
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        ));
                break;
            case CALORIES:
                macronutrientHistory = statisticHistoryMap
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, DietStatisticHistory>comparingByKey().reversed())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().getCalories(),
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        ));
                break;
            default:
                throw new IllegalArgumentException("Macronutrient with type = [" + macronutrient + "] is not supported");
        }

        int accuracy = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getRoundAccuracy();
        average = round(macronutrientHistory
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0), accuracy);
        median = round(
                statisticProcessorsManager.getStatisticProcessors().get(StatisticType.MEDIAN).calculate(new ArrayList<>(macronutrientHistory
                        .values())), accuracy);

        return new StatisticHistoryResult()
                .setMacronutrient(macronutrient)
                .setDuration(duration)
                .setHistory(macronutrientHistory)
                .setAverage(average)
                .setMedian(median);
    }

    private DateRange getDateRange(Statistic statistic) {
        LocalDate startDate = statistic.getDateRange().getStartDate();
        LocalDate endDate = statistic.getDateRange().getEndDate();
        Duration duration = statistic.getDateRange().getDuration();

        DateRange findDateRange;

        if (statistic.getDateRange().getDuration() != null) {
            switch (Objects.requireNonNull(duration)) {
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
