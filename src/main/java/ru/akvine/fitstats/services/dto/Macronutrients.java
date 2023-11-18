package ru.akvine.fitstats.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Macronutrients {
    private final double proteins;
    private final double fats;
    private final double carbohydrates;
    private final double calories;
}
