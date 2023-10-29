package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateStatisticRequest;

import javax.validation.Valid;

@RequestMapping(value = "/statistics")
public interface StatisticControllerMeta {
    @GetMapping("/calculate")
    Response calculate(@Valid @RequestBody CalculateStatisticRequest request);
}
