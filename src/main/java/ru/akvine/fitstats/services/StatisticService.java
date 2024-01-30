package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.Macronutrient;
import ru.akvine.fitstats.enums.StatisticType;
import ru.akvine.fitstats.repositories.DietRecordRepository;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.statistic.*;
import ru.akvine.fitstats.services.processors.macronutrient.MacronutrientProcessor;
import ru.akvine.fitstats.services.processors.statistic.additional.ModeStatisticProcessor;
import ru.akvine.fitstats.services.processors.statistic.additional.PercentStatisticProcessor;
import ru.akvine.fitstats.services.processors.statistic.main.StatisticProcessor;
import ru.akvine.fitstats.utils.MathUtils;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static ru.akvine.fitstats.utils.DateUtils.*;

@Service
@Slf4j
public class StatisticService {
    private static final String FATS_MACRONUTRIENT_NAME = "fats";
    private static final String PROTEINS_MACRONUTRIENT_NAME = "proteins";
    private static final String CARBOHYDRATES_MACRONUTRIENT_NAME = "carbohydrates";
    private static final String ALCOHOL_NAME = "alcohol";

    private static final int FATS_MACRONUTRIENT_CALORIES_COEFFICIENT = 9;
    private static final int PROTEINS_MACRONUTRIENT_CALORIES_COEFFICIENT = 4;
    private static final int CARBOHYDRATES_MACRONUTRIENT_CALORIES_COEFFICIENT = 4;

    private static final int MONTH_AVERAGE_COUNT = 30;

    private final DietRecordRepository dietRecordRepository;
    private final ClientService clientService;
    private final ModeStatisticProcessor modeStatisticProcessor;
    private final PercentStatisticProcessor percentStatisticProcessor;
    private final Map<StatisticType, StatisticProcessor> availableStatisticProcessors;
    private final Map<Macronutrient, MacronutrientProcessor> availableMacronutrientProcessors;

    @Value("${round.accuracy}")
    private int roundAccuracy;

    @Autowired
    public StatisticService(List<StatisticProcessor> statisticProcessors,
                            List<MacronutrientProcessor> macronutrientProcessors,
                            ClientService clientService,
                            DietRecordRepository dietRecordRepository,
                            ModeStatisticProcessor modeStatisticProcessor,
                            PercentStatisticProcessor percentStatisticProcessor) {
        this.dietRecordRepository = dietRecordRepository;
        this.clientService = clientService;
        this.modeStatisticProcessor = modeStatisticProcessor;
        this.percentStatisticProcessor = percentStatisticProcessor;
        this.availableStatisticProcessors =
                statisticProcessors
                        .stream()
                        .collect(toMap(StatisticProcessor::getType, identity()));
        this.availableMacronutrientProcessors =
                macronutrientProcessors
                        .stream()
                        .collect(toMap(MacronutrientProcessor::getType, identity()));
    }

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
                                List<Double> macronutrientsValues = availableMacronutrientProcessors
                                        .get(macronutrient)
                                        .extract(records);
                                double statisticValue = availableStatisticProcessors
                                        .get(indicator)
                                        .calculate(macronutrientsValues);
                                statsInfo.put(macronutrient.name().toLowerCase(), MathUtils.round(statisticValue, roundAccuracy));
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

        List<Double> proteinsValues = availableMacronutrientProcessors
                .get(Macronutrient.PROTEINS)
                .extract(records)
                .stream()
                .map(value -> value * PROTEINS_MACRONUTRIENT_CALORIES_COEFFICIENT)
                .collect(Collectors.toList());
        List<Double> fatsValues = availableMacronutrientProcessors
                .get(Macronutrient.FATS)
                .extract(records)
                .stream()
                .map(value -> value * FATS_MACRONUTRIENT_CALORIES_COEFFICIENT)
                .collect(Collectors.toList());
        List<Double> carbohydrates = availableMacronutrientProcessors
                .get(Macronutrient.CARBOHYDRATES)
                .extract(records)
                .stream()
                .map(value -> value * CARBOHYDRATES_MACRONUTRIENT_CALORIES_COEFFICIENT)
                .collect(Collectors.toList());
        double totalCalories = availableMacronutrientProcessors
                .get(Macronutrient.CALORIES)
                .extract(records)
                .stream()
                .mapToDouble(value -> value)
                .sum();

        double proteinsPercent = MathUtils.round(
                percentStatisticProcessor.calculate(proteinsValues, totalCalories), roundAccuracy);
        double fatsPercent = MathUtils.round(
                percentStatisticProcessor.calculate(fatsValues, totalCalories), roundAccuracy);
        double carbohydratesPercent = MathUtils.round(
                percentStatisticProcessor.calculate(carbohydrates, totalCalories), roundAccuracy);
        double alcoholCaloriesPercent = MathUtils.round((100 - (proteinsPercent + fatsPercent + carbohydratesPercent)), roundAccuracy);

        macronutrientsPercents.put(PROTEINS_MACRONUTRIENT_NAME, proteinsPercent);
        macronutrientsPercents.put(FATS_MACRONUTRIENT_NAME, fatsPercent);
        macronutrientsPercents.put(CARBOHYDRATES_MACRONUTRIENT_NAME, carbohydratesPercent);
        macronutrientsPercents.put(ALCOHOL_NAME, alcoholCaloriesPercent);

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
                statisticHistoryMap = calculatePerPastDays(records);
                break;
            case WEEK:
                statisticHistoryMap = calculateWeeksPerHalfYear(records);
                break;
            case MONTH:
                statisticHistoryMap = calculatePastMonths(records);
                break;
            case YEAR:
                statisticHistoryMap = calculatePastFiveYears(records);
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

        average = MathUtils.round(macronutrientHistory
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0), roundAccuracy);
        median = MathUtils.round(
                availableStatisticProcessors.get(StatisticType.MEDIAN).calculate(new ArrayList<>(macronutrientHistory
                        .values())), roundAccuracy);

        return new StatisticHistoryResult()
                .setMacronutrient(macronutrient)
                .setDuration(duration)
                .setHistory(macronutrientHistory)
                .setAverage(average)
                .setMedian(median);
    }

    private Map<String, DietStatisticHistory> calculatePerPastDays(List<DietRecordEntity> entities) {
        return entities.stream()
                .filter(entity -> entity.getDate().isAfter(LocalDate.now().minusDays(MONTH_AVERAGE_COUNT)))
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
                                        entity.getCalories()
                                ),
                                (agg1, agg2) -> new DietStatisticHistory(
                                        agg1.getDate(),
                                        MathUtils.round(agg1.getProteins() + agg2.getProteins(), roundAccuracy),
                                        MathUtils.round(agg1.getFats() + agg2.getFats(), roundAccuracy),
                                        MathUtils.round(agg1.getCarbohydrates() + agg2.getCarbohydrates(), roundAccuracy),
                                        MathUtils.round(agg1.getCalories() + agg2.getCalories(), roundAccuracy)
                                )
                        )
                ));
    }

    private Map<String, DietStatisticHistory> calculateWeeksPerHalfYear(List<DietRecordEntity> entities) {
        return entities.stream()
                .filter(entity -> entity.getDate().isAfter(LocalDate.now().minusMonths(6)))
                .collect(Collectors.groupingBy(
                        entity -> String.valueOf(entity.getDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)),
                        Collectors.reducing(
                                new DietStatisticHistory(),
                                entity -> new DietStatisticHistory(
                                        String.valueOf(entity.getDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)),
                                        entity.getProteins(),
                                        entity.getFats(),
                                        entity.getCarbohydrates(),
                                        entity.getCalories()
                                ),
                                (agg1, agg2) -> new DietStatisticHistory(
                                        agg1.getDate(),
                                        MathUtils.round(agg1.getProteins() + agg2.getProteins(), roundAccuracy),
                                        MathUtils.round(agg1.getFats() + agg2.getFats(), roundAccuracy),
                                        MathUtils.round(agg1.getCarbohydrates() + agg2.getCarbohydrates(), roundAccuracy),
                                        MathUtils.round(agg1.getCalories() + agg2.getCalories(), roundAccuracy)
                                )
                        )
                ));
    }

    private Map<String, DietStatisticHistory> calculatePastMonths(List<DietRecordEntity> entities) {
        return entities.stream()
                .filter(entity -> entity.getDate().isAfter(LocalDate.now().minusYears(1)))
                .collect(Collectors.groupingBy(
                        entity -> String.valueOf(entity.getDate().getMonthValue()),
                        Collectors.reducing(
                                new DietStatisticHistory(),
                                entity -> new DietStatisticHistory(
                                        String.valueOf(entity.getDate().getMonthValue()),
                                        entity.getProteins(),
                                        entity.getFats(),
                                        entity.getCarbohydrates(),
                                        entity.getCalories()
                                ),
                                (agg1, agg2) -> new DietStatisticHistory(
                                        agg1.getDate(),
                                        MathUtils.round(agg1.getProteins() + agg2.getProteins(), roundAccuracy),
                                        MathUtils.round(agg1.getFats() + agg2.getFats(), roundAccuracy),
                                        MathUtils.round(agg1.getCarbohydrates() + agg2.getCarbohydrates(), roundAccuracy),
                                        MathUtils.round(agg1.getCalories() + agg2.getCalories(), roundAccuracy)
                                )
                        )
                ));
    }

    private Map<String, DietStatisticHistory> calculatePastFiveYears(List<DietRecordEntity> entities) {
        return entities.stream()
                .filter(entity -> entity.getDate().isAfter(LocalDate.now().minusYears(5)))
                .collect(Collectors.groupingBy(
                        entity -> String.valueOf(entity.getDate().getYear()),
                        Collectors.reducing(
                                new DietStatisticHistory(),
                                entity -> new DietStatisticHistory(
                                        String.valueOf(entity.getDate().getYear()),
                                        entity.getProteins(),
                                        entity.getFats(),
                                        entity.getCarbohydrates(),
                                        entity.getCalories()
                                ),
                                (agg1, agg2) -> new DietStatisticHistory(
                                        agg1.getDate(),
                                        MathUtils.round(agg1.getProteins() + agg2.getProteins(), roundAccuracy),
                                        MathUtils.round(agg1.getFats() + agg2.getFats(), roundAccuracy),
                                        MathUtils.round(agg1.getCarbohydrates() + agg2.getCarbohydrates(), roundAccuracy),
                                        MathUtils.round(agg1.getCalories() + agg2.getCalories(), roundAccuracy)
                                )
                        )
                ));
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
