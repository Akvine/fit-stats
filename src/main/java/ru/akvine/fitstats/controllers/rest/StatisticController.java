package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.StatisticConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateAdditionalStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateMainStatisticRequest;
import ru.akvine.fitstats.controllers.rest.meta.StatisticControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.StatisticValidator;
import ru.akvine.fitstats.services.StatisticService;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatistic;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatisticInfo;
import ru.akvine.fitstats.services.dto.statistic.MainStatistic;
import ru.akvine.fitstats.services.dto.statistic.MainStatisticInfo;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatisticController implements StatisticControllerMeta {
    private final StatisticValidator statisticValidator;
    private final StatisticService statisticService;
    private final StatisticConverter statisticConverter;

    @Override
    public Response main(@Valid CalculateMainStatisticRequest request) {
        statisticValidator.verifyCalculateMainStatisticRequest(request);
        MainStatistic mainStatistic = statisticConverter.convertToMainStatistic(request);
        MainStatisticInfo mainStatisticInfo = statisticService.calculateMainStatisticInfo(mainStatistic);
        return statisticConverter.convertToMainStatisticResponse(mainStatisticInfo);
    }

    @Override
    public Response additional(@Valid CalculateAdditionalStatisticRequest request) {
        statisticValidator.verifyCalculateAdditionalStatisticRequest(request);
        AdditionalStatistic additionalStatistic = statisticConverter.convertToAdditionalStatistic(request);
        AdditionalStatisticInfo additionalStatisticInfo = statisticService.calculateAdditionalStatisticInfo(additionalStatistic);
        return statisticConverter.convertAdditionalStatisticResponse(additionalStatisticInfo);
    }
}
