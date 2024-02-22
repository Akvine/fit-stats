package ru.akvine.fitstats.services;

import org.springframework.stereotype.Service;
import ru.akvine.fitstats.services.dto.Macronutrients;

import static ru.akvine.fitstats.constants.MacronutrientsConstants.*;

@Service
public class MacronutrientsCalculationService {
    private static final double ZERO = 0;

    public double calculateCalories(double proteins) {
        return calculateCalories(proteins, ZERO);
    }

    public double calculateCalories(double proteins,
                                    double fats) {
        return calculateCalories(proteins, fats, ZERO);
    }

    public double calculateCalories(double proteins,
                                    double fats,
                                    double carbohydrates) {
        return calculateCalories(proteins, fats, carbohydrates, ZERO);
    }

    public double calculateCalories(double proteins,
                                    double fats,
                                    double carbohydrates,
                                    double alcohol) {
        return proteins * PROTEINS_MACRONUTRIENT_CALORIES_COEFFICIENT +
                fats * FATS_MACRONUTRIENT_CALORIES_COEFFICIENT +
                carbohydrates * CARBOHYDRATES_MACRONUTRIENT_CALORIES_COEFFICIENT +
                alcohol * ALCOHOL_MACRONUTRIENT_CALORIES_COEFFICIENT;
    }

    public Macronutrients calculatePer100(double proteins,
                                          double fats,
                                          double carbohydrates,
                                          double alcohol,
                                          double volume) {
        double proteinsPer100 = proteins * DEFAULT_VOLUME / volume;
        double fatsPer100 = fats * DEFAULT_VOLUME / volume;
        double carbohydratesPer100 = carbohydrates * DEFAULT_VOLUME / volume;
        double alcoholPer100 = alcohol * DEFAULT_VOLUME / volume;
        double caloriesPer100 = calculateCalories(proteinsPer100, fatsPer100, carbohydratesPer100, alcoholPer100);
        return new Macronutrients(proteinsPer100, fatsPer100, carbohydratesPer100, alcoholPer100, caloriesPer100);
    }

    public Macronutrients calculate(double proteinsPer100,
                                    double fatsPer100,
                                    double carbohydratesPer100,
                                    double alcoholPer100,
                                    double volume) {
        double proteins = proteinsPer100 * volume / DEFAULT_VOLUME;
        double fats = fatsPer100 * volume / DEFAULT_VOLUME;
        double carbohydrates = carbohydratesPer100 * volume / DEFAULT_VOLUME;
        double alcohol = alcoholPer100 * volume / DEFAULT_VOLUME;
        double calories = calculateCalories(proteins, fats, carbohydrates, alcohol);
        return new Macronutrients(proteins, fats, carbohydrates, alcohol, calories);
    }
}
