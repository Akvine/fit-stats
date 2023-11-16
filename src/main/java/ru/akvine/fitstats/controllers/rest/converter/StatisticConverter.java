package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.statistic.AdditionalStatisticResponse;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateAdditionalStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateMainStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.MainStatisticResponse;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatistic;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatisticInfo;
import ru.akvine.fitstats.services.dto.statistic.MainStatistic;
import ru.akvine.fitstats.services.dto.statistic.MainStatisticInfo;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatisticConverter {
    @Value("${round.accuracy}")
    private int defaultRoundAccuracy;
    @Value("${product.statistic.mode.count.limit}")
    private int limit;

    public MainStatistic convertToMainStatistic(CalculateMainStatisticRequest request) {
        Preconditions.checkNotNull("calculateStatisticRequest is null");

        String duration = request
                .getDateRangeInfo()
                .getDuration();
        Map<String, List<String>> indicatorsWithMacronutrients = new LinkedHashMap<>();
        request
                .getIndicators()
                .forEach(indicator -> indicatorsWithMacronutrients.put(indicator, request.getMacronutrients()));
        return MainStatistic
                .builder()
                .clientUuid(SecurityUtils.getCurrentUser().getUuid())
                .dateRange(new DateRange()
                        .setDuration(StringUtils.isBlank(duration) ? null : Duration.valueOf(duration))
                        .setStartDate(request.getDateRangeInfo().getStartDate())
                        .setEndDate(request.getDateRangeInfo().getEndDate()))
                .indicatorsWithMacronutrients(indicatorsWithMacronutrients)
                .roundAccuracy(request.getRoundAccuracy() == null ? defaultRoundAccuracy : request.getRoundAccuracy())
                .build();
    }

    public MainStatisticResponse convertToMainStatisticResponse(MainStatisticInfo mainStatisticInfo) {
        Preconditions.checkNotNull(mainStatisticInfo, "statisticMainInfo is null");
        return new MainStatisticResponse()
                .setInfo(mainStatisticInfo);
    }

    public AdditionalStatistic convertToAdditionalStatistic(CalculateAdditionalStatisticRequest request) {
        Preconditions.checkNotNull(request, "calculateAdditionalStatisticRequest is null");

        String duration = request
                .getDateRangeInfo()
                .getDuration();

        return AdditionalStatistic
                .builder()
                .clientUuid(SecurityUtils.getCurrentUser().getUuid())
                .modeCount(request.getModeCount() != null ? request.getModeCount() : limit)
                .dateRange(new DateRange()
                        .setDuration(StringUtils.isBlank(duration) ? null : Duration.valueOf(duration))
                        .setStartDate(request.getDateRangeInfo().getStartDate())
                        .setEndDate(request.getDateRangeInfo().getEndDate()))
                .roundAccuracy(request.getRoundAccuracy() == null ? defaultRoundAccuracy : request.getRoundAccuracy())
                .build();
    }

    public AdditionalStatisticResponse convertAdditionalStatisticResponse(AdditionalStatisticInfo additionalStatisticInfo) {
        Preconditions.checkNotNull(additionalStatisticInfo, "additionalStatisticInfo is null");
        return new AdditionalStatisticResponse()
                .setInfo(additionalStatisticInfo);
    }
}
