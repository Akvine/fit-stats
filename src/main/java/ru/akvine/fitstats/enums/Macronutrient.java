package ru.akvine.fitstats.enums;

public enum Macronutrient {
    PROTEINS,
    FATS,
    CARBOHYDRATES,
    ALCOHOL,
    CALORIES;

    public static Macronutrient safeValueOf(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Macronutrient value is null");
        }

        switch (value.toLowerCase()) {
            case "calories": return CALORIES;
            case "proteins": return PROTEINS;
            case "fats": return FATS;
            case "carbohydrates": return CARBOHYDRATES;
            case "alcohol": return ALCOHOL;
            default: throw new IllegalStateException("Macronutrient with value = [" + value + "] is not supported!");
        }
    }
}
