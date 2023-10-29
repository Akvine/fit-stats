package ru.akvine.fitstats.utils;

import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.enums.HeightMeasurement;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.enums.WeightMeasurement;
import ru.akvine.fitstats.services.dto.Macronutrients;

import javax.validation.constraints.NotNull;

public class DietUtils {
    private static final int FATS_CALORIES = 9;
    private static final int PROTEINS_CALORIES = 4;
    private static final int CARBOHYDRATES_CALORIES = 4;

    private static final double IB_COEFFICIENT = 0.45359237;
    private static final double OZ_COEFFICIENT = 0.0283495;

    private static final double FT_COEFFICIENT = 30.48;
    private static final double IN_COEFFICIENT = 2.54;

    private static final int DEFAULT_VOLUME = 100;

    @NotNull
    public static Macronutrients calculateMacronutrients(
            double proteinsPer100,
            double fatsPer100,
            double carbohydratesPer100) {
        return calculateMacronutrients(proteinsPer100, fatsPer100, carbohydratesPer100, DEFAULT_VOLUME);
    }

    @NotNull
    public static Macronutrients calculateMacronutrients(
            double proteinsPer100,
            double fatsPer100,
            double carbohydratesPer100,
            double grams) {
        double proteins = proteinsPer100 * grams / DEFAULT_VOLUME;
        double fats = fatsPer100 * grams / DEFAULT_VOLUME;
        double carbohydrates = carbohydratesPer100 * grams / DEFAULT_VOLUME;
        double calories = calculateCalories(proteins, fats, carbohydrates);
        return new Macronutrients(proteins, fats, carbohydrates, calories);
    }

    public static Macronutrients transformPer100(double proteins,
                                                 double fats,
                                                 double carbohydrates,
                                                 double volume) {
        double proteinsPer100 = proteins * DEFAULT_VOLUME / volume;
        double fatsPer100 = fats * DEFAULT_VOLUME / volume;
        double carbohydratesPer100 = carbohydrates * DEFAULT_VOLUME / volume;
        double caloriesPer100 = calculateCalories(proteinsPer100, fatsPer100, carbohydratesPer100);
        return new Macronutrients(proteinsPer100, fatsPer100, carbohydratesPer100, caloriesPer100);
    }

    public static double calculateCalories(double proteins, double fats, double carbohydrates) {
        return fats * FATS_CALORIES + proteins * PROTEINS_CALORIES + carbohydrates * CARBOHYDRATES_CALORIES;
    }

    /**
     * Calculate basic exchange
     *
     * @param gender
     * @param age
     * @param weight in kg
     * @param height in cm
     * @return
     */
    public static double calculateBasicExchange(Gender gender,
                                                int age,
                                                double height,
                                                double weight) {
        switch (gender) {
            case MALE:
                return (10 * weight) + (6.25 * height) - (5 * age) + 5;
            case FEMALE:
                return (10 * weight) + (6.25 * height) - (5 * age) - 161;
            default:
                throw new IllegalStateException("Gender [" + gender + "] is not supported!");
        }
    }

    /**
     * Calculate daily calories intake
     *
     * @param basicExchangeCalories
     * @param physicalActivity
     * @return
     */
    public static double calculateDailyCaloriesIntake(double basicExchangeCalories, PhysicalActivity physicalActivity) {
        return basicExchangeCalories * physicalActivity.getValue();
    }

    public static double convertToKg(double weight, WeightMeasurement measurement) {
        switch (measurement) {
            case IB:
                return weight * IB_COEFFICIENT;
            case OZ:
                return weight * OZ_COEFFICIENT;
            case KG:
                return weight;
            default:
                throw new IllegalStateException("Weight type measurement = [" + measurement + "] is not supported!");
        }
    }

    public static double convertToCm(double height, HeightMeasurement measurement) {
        switch (measurement) {
            case FT:
                return height * FT_COEFFICIENT;
            case IN:
                return height * IN_COEFFICIENT;
            case CM:
                return height;
            default:
                throw new IllegalStateException("Weight type measurement = [" + measurement + "] is not supported!");
        }
    }
}
