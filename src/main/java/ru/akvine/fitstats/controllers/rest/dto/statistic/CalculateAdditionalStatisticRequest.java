package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CalculateAdditionalStatisticRequest extends DateRangeRequest {
    private Integer modeCount;

    private Integer roundAccuracy;
}
