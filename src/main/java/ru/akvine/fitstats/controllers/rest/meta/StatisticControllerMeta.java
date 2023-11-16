package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateAdditionalStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateMainStatisticRequest;

import javax.validation.Valid;

@RequestMapping(value = "/statistics")
public interface StatisticControllerMeta {
    @GetMapping(value = "/main/calculate")
    Response main(@Valid @RequestBody CalculateMainStatisticRequest request);

    @GetMapping(value = "/additional/calculate")
    Response additional(@Valid @RequestBody CalculateAdditionalStatisticRequest request);
}
