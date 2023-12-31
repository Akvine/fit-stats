package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatisticInfo;

@Data
@Accessors(chain = true)
public class AdditionalStatisticResponse extends SuccessfulResponse {
    private AdditionalStatisticInfo info;
}
