package ru.akvine.fitstats.controllers.rest.dto.statistic;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class DietRecordDto {
    private String productUuid;
    private String productTitle;
    private String measurement;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double calories;
    private double volume;
}
