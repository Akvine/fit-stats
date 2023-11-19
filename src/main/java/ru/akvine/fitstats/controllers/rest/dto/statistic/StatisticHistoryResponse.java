package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class StatisticHistoryResponse extends SuccessfulResponse {
    private StatisticHistoryInfo info;
}
