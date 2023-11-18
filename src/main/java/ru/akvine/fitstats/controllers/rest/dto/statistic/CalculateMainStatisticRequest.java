package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CalculateMainStatisticRequest extends DateRangeRequest {
    private List<String> indicators;
    private List<String> macronutrients;
    private Integer roundAccuracy;
}
