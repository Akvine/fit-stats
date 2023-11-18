package ru.akvine.fitstats.controllers.rest.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DietDisplayInfoDto {
    private int maxProteins;

    private int maxFats;

    private int maxCarbohydrates;

    private int maxCalories;

    private int currentProteins;

    private int currentFats;

    private int currentCarbohydrates;

    private int currentCalories;

    private int remainingProteins;

    private int remainingFats;

    private int remainingCarbohydrates;

    private int remainingCalories;
}
