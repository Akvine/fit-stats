package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DateRangeRequest {
    private DateRangeInfo dateRangeInfo;
}
