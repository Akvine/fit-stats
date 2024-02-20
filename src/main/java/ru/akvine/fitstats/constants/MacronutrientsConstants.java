package ru.akvine.fitstats.constants;

public class MacronutrientsConstants {
    private MacronutrientsConstants() {
        throw new IllegalStateException("Call MacronutrientsConstants constructor is prohibited!");
    }

    public static final double FATS_MACRONUTRIENT_CALORIES_COEFFICIENT = 9.3;
    public static final double PROTEINS_MACRONUTRIENT_CALORIES_COEFFICIENT = 4.1;
    public static final double CARBOHYDRATES_MACRONUTRIENT_CALORIES_COEFFICIENT = 4.1;
    public static final double ALCOHOL_MACRONUTRIENT_CALORIES_COEFFICIENT = 7.1;
}
