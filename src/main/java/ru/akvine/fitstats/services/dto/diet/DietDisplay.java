package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DietDisplay {
    private double maxCalories;
    private double maxProteins;
    private double maxFats;
    private double maxCarbohydrates;
    private double currentCalories;
    private double currentProteins;
    private double currentFats;
    private double currentCarbohydrates;
    private double remainingProteins;
    private double remainingFats;
    private double remainingCarbohydrates;
    private double remainingCalories;
}
