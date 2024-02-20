package ru.akvine.fitstats.controllers.rest.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DietDisplayInfoDto {
    private double maxProteins;

    private double maxFats;

    private double maxCarbohydrates;

    private double maxCalories;

    private double currentProteins;

    private double currentFats;

    private double currentCarbohydrates;

    private double currentCalories;

    private double remainingProteins;

    private double remainingFats;

    private double remainingCarbohydrates;

    private double remainingCalories;
}
