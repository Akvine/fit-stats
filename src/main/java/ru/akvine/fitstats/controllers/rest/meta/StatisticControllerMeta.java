package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateAdditionalStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateDescriptiveStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.StatisticHistoryRequest;

import javax.validation.Valid;

@RequestMapping(value = "/statistics")
public interface StatisticControllerMeta {
    @GetMapping(value = "/descriptive/calculate")
    Response descriptive(@Valid @RequestBody CalculateDescriptiveStatisticRequest request);

    @GetMapping(value = "/additional/calculate")
    Response additional(@Valid @RequestBody CalculateAdditionalStatisticRequest request);

    @GetMapping(value = "/history")
    Response history(@Valid @RequestBody StatisticHistoryRequest request);
}
