package ru.akvine.fitstats.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Macronutrients {
    private final double proteins;
    private final double fats;
    private final double carbohydrates;
    private final double calories;
}
