package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.services.dto.statistic.DescriptiveStatisticInfo;

@Data
@Accessors(chain = true)
public class DescriptiveStatisticResponse extends SuccessfulResponse {
    private DescriptiveStatisticInfo info;
}
