package ru.akvine.fitstats.utils;

import com.google.common.base.Preconditions;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.BiometricBean;

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

    private final static double GAIN_PROTEIN_COEFFICIENT = 1.7;
    private final static double GAIN_FATS_COEFFICIENT = 1.2;
    private final static double DRYING_FATS_COEFFICIENT = 0.7;

    private final static int MALE_HEIGHT_COEFFICIENT = 100;
    private final static int FEMALE_HEIGHT_COEFFICIENT = 110;

    private final static int PROTEINS_CALORIES_COEFFICIENT = 4;
    private final static int CARBOHYDRATES_CALORIES_COEFFICIENT = 4;;
    private final static int FATS_CALORIES_COEFFICIENT = 9;

    private final static int VOL_ZERO = 0;

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

    public static double calculateDailyCaloriesIntake(double basicExchangeCalories, PhysicalActivity physicalActivity) {
        return basicExchangeCalories * physicalActivity.getValue();
    }

    public static Macronutrients calculate(BiometricBean biometricBean, Diet diet) {
        Preconditions.checkNotNull(biometricBean, "biometricBean is null");
        Preconditions.checkNotNull(diet, "diet is null");

        Gender gender = biometricBean.getGender();
        PhysicalActivity physicalActivity = biometricBean.getPhysicalActivity();
        int age = biometricBean.getAge();
        double height = Double.parseDouble(biometricBean.getHeight());
        double weight = Double.parseDouble(biometricBean.getWeight());

        double basicExchange = DietUtils.calculateBasicExchange(gender, age, height, weight);
        double dailyCaloriesIntake = DietUtils.calculateDailyCaloriesIntake(basicExchange, physicalActivity);

        double maxCalories = dailyCaloriesIntake;
        double maxProteins;
        double maxFats;
        double maxCarbohydrates;

        switch (diet) {
            case GAIN:
                maxCalories = dailyCaloriesIntake + dailyCaloriesIntake * 0.15;
                maxProteins = weight * GAIN_PROTEIN_COEFFICIENT;
                maxFats = weight * GAIN_FATS_COEFFICIENT;
                break;
            case RETENTION:
                maxProteins = weight;
                maxFats = weight;
                break;
            case DRYING:
                maxCalories = dailyCaloriesIntake - dailyCaloriesIntake * 0.2;
                maxProteins = gender == Gender.MALE ? (height - MALE_HEIGHT_COEFFICIENT) * GAIN_PROTEIN_COEFFICIENT
                        : (height - FEMALE_HEIGHT_COEFFICIENT) * GAIN_PROTEIN_COEFFICIENT;
                maxFats = gender == Gender.MALE ? (height - MALE_HEIGHT_COEFFICIENT) * DRYING_FATS_COEFFICIENT : (height - FEMALE_HEIGHT_COEFFICIENT) * DRYING_FATS_COEFFICIENT;
                break;
            default:
                throw new IllegalStateException("Diet type = [" + diet + "] is not supported!");
        }
        maxCarbohydrates = (maxCalories - (maxProteins * PROTEINS_CALORIES_COEFFICIENT + maxFats * FATS_CALORIES_COEFFICIENT)) / CARBOHYDRATES_CALORIES_COEFFICIENT;
        return new Macronutrients(maxProteins, maxFats, maxCarbohydrates, VOL_ZERO, maxCalories);
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
