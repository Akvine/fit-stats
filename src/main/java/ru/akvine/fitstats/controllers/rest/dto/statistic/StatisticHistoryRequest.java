package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class StatisticHistoryRequest {
    @NotBlank
    private String duration;

    @NotBlank
    private String macronutrient;
}
