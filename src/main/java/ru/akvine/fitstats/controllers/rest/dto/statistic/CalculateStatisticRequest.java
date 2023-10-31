package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class CalculateStatisticRequest {
    private List<String> indicators;
    private List<String> macronutrients;
    private DateRangeInfo dateRangeInfo;
    private Integer roundAccuracy;
}