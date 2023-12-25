package ru.akvine.fitstats.utils;

import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.services.dto.Macronutrients;

import javax.validation.constraints.NotNull;

public class DietUtils {
    private static final int FATS_CALORIES = 9;
    private static final int PROTEINS_CALORIES = 4;
    private static final int CARBOHYDRATES_CALORIES = 4;
    private static final double ALCOHOL_CALORIES = 5.6;

    private static final int DEFAULT_VOLUME = 100;

    private static final double HEIGHT_COEFFICIENT = 6.25;
    private static final double WEIGHT_COEFFICIENT = 10;
    private static final double AGE_COEFFICIENT = 5;

    @NotNull
    public static Macronutrients calculateMacronutrients(
            double proteinsPer100,
            double fatsPer100,
            double carbohydratesPer100,
            double volPer100) {
        return calculateMacronutrients(proteinsPer100, fatsPer100, carbohydratesPer100, volPer100, DEFAULT_VOLUME);
    }

    @NotNull
    public static Macronutrients calculateMacronutrients(
            double proteinsPer100,
            double fatsPer100,
            double carbohydratesPer100,
            double volPer100,
            double grams) {
        return getMacronutrients(proteinsPer100, fatsPer100, carbohydratesPer100, volPer100, DEFAULT_VOLUME, grams);
    }

    public static Macronutrients transformPer100(double proteins,
                                                 double fats,
                                                 double carbohydrates,
                                                 double vol,
                                                 double volume) {
        return getMacronutrients(proteins, fats, carbohydrates, vol, volume, DEFAULT_VOLUME);
    }

    public static double calculateCalories(double proteins, double fats, double carbohydrates, double vol) {
        return fats * FATS_CALORIES + proteins * PROTEINS_CALORIES + carbohydrates * CARBOHYDRATES_CALORIES + vol * ALCOHOL_CALORIES;
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
                return (WEIGHT_COEFFICIENT * weight) + (HEIGHT_COEFFICIENT * height) - (AGE_COEFFICIENT * age) + 5;
            case FEMALE:
                return (WEIGHT_COEFFICIENT * weight) + (HEIGHT_COEFFICIENT * height) - (AGE_COEFFICIENT * age) - 161;
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

    private static Macronutrients getMacronutrients(double proteins, double fats, double carbohydrates, double vol, double volume, double defaultVolume) {
        double proteinsPer100 = proteins * defaultVolume / volume;
        double fatsPer100 = fats * defaultVolume / volume;
        double carbohydratesPer100 = carbohydrates * defaultVolume / volume;
        double volPer100 = vol * defaultVolume / volume;
        double caloriesPer100 = calculateCalories(proteinsPer100, fatsPer100, carbohydratesPer100, volPer100);
        return new Macronutrients(proteinsPer100, fatsPer100, carbohydratesPer100, volPer100, caloriesPer100);
    }
}
