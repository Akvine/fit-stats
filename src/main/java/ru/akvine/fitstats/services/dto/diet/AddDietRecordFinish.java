package ru.akvine.fitstats.services.dto.diet;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AddDietRecordFinish {
    private String productUuid;
    private String productTitle;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double calories;
}
