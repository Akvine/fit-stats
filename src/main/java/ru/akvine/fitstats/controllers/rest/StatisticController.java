package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.StatisticConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateAdditionalStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateDescriptiveStatisticRequest;
import ru.akvine.fitstats.controllers.rest.meta.StatisticControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.StatisticValidator;
import ru.akvine.fitstats.services.StatisticService;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatistic;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatisticInfo;
import ru.akvine.fitstats.services.dto.statistic.DescriptiveStatistic;
import ru.akvine.fitstats.services.dto.statistic.DescriptiveStatisticInfo;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatisticController implements StatisticControllerMeta {
    private final StatisticValidator statisticValidator;
    private final StatisticService statisticService;
    private final StatisticConverter statisticConverter;

    @Override
    public Response descriptive(@Valid CalculateDescriptiveStatisticRequest request) {
        statisticValidator.verifyCalculateDescriptiveStatisticRequest(request);
        DescriptiveStatistic descriptiveStatistic = statisticConverter.convertToDescriptiveStatistic(request);
        DescriptiveStatisticInfo descriptiveStatisticInfo = statisticService.calculateDescriptiveStatisticInfo(descriptiveStatistic);
        return statisticConverter.convertToDescriptiveStatisticResponse(descriptiveStatisticInfo);
    }

    @Override
    public Response additional(@Valid CalculateAdditionalStatisticRequest request) {
        statisticValidator.verifyCalculateAdditionalStatisticRequest(request);
        AdditionalStatistic additionalStatistic = statisticConverter.convertToAdditionalStatistic(request);
        AdditionalStatisticInfo additionalStatisticInfo = statisticService.calculateAdditionalStatisticInfo(additionalStatistic);
        return statisticConverter.convertAdditionalStatisticResponse(additionalStatisticInfo);
    }
}
