package ru.akvine.fitstats.utils;

import com.google.common.base.Preconditions;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.BiometricBean;

import javax.validation.constraints.NotNull;

public class DietUtils {
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
}
