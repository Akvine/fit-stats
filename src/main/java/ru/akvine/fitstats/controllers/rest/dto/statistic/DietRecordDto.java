package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DietRecordDto {
    private String uuid;
    private String productUuid;
    private String productTitle;
    private String measurement;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double calories;
    private double volume;
}
