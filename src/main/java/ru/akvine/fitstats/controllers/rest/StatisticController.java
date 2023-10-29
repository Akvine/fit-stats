package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.StatisticConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateStatisticRequest;
import ru.akvine.fitstats.controllers.rest.meta.StatisticControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.StatisticValidator;
import ru.akvine.fitstats.services.StatisticService;
import ru.akvine.fitstats.services.dto.statistic.Statistic;
import ru.akvine.fitstats.services.dto.statistic.StatisticInfo;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatisticController implements StatisticControllerMeta {
    private final StatisticValidator statisticValidator;
    private final StatisticService statisticService;
    private final StatisticConverter statisticConverter;

    @Override
    public Response calculate(@Valid CalculateStatisticRequest request) {
        statisticValidator.verifyCalculateStatisticRequest(request);
        Statistic statistic = statisticConverter.convertToStatistic(request);
        StatisticInfo statisticInfo = statisticService.calculateStatisticInfo(statistic);
        return statisticConverter.convertToStatisticResponse(statisticInfo);
    }
}
