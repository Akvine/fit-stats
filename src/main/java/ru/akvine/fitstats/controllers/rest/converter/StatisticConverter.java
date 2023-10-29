package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.StatisticResponse;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.statistic.Statistic;
import ru.akvine.fitstats.services.dto.statistic.StatisticInfo;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatisticConverter {
    @Value("${round.accuracy}")
    private int defaultRoundAccuracy;

    public Statistic convertToStatistic(CalculateStatisticRequest request) {
        Preconditions.checkNotNull("calculateStatisticRequest is null");

        String duration = request
                .getDateRangeInfo()
                .getDuration();
        Map<String, List<String>> indicatorsWithMacronutrients = new LinkedHashMap<>();
        request
                .getIndicators()
                .forEach(indicator -> indicatorsWithMacronutrients.put(indicator, request.getMacronutrients()));
        return new Statistic()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setDateRange(new DateRange()
                        .setDuration(StringUtils.isBlank(duration) ? null : Duration.valueOf(duration))
                        .setStartDate(request.getDateRangeInfo().getStartDate())
                        .setEndDate(request.getDateRangeInfo().getEndDate()))
                .setIndicatorsWithMacronutrients(indicatorsWithMacronutrients)
                .setRoundAccuracy(request.getRoundAccuracy() == null ? defaultRoundAccuracy : request.getRoundAccuracy());
    }

    public StatisticResponse convertToStatisticResponse(StatisticInfo statisticInfo) {
        Preconditions.checkNotNull(statisticInfo, "statisticInfo is null");
        return new StatisticResponse()
                .setStatisticInfo(statisticInfo);
    }
}
