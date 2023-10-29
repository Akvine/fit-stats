package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.services.dto.statistic.StatisticInfo;

@Getter
@Setter
@Accessors(chain = true)
public class StatisticResponse extends SuccessfulResponse {
    private StatisticInfo statisticInfo;
}
