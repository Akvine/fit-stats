package ru.akvine.fitstats.enums;

public enum PhysicalActivity {
    /**
     * For sedentary people without physical activity or with their minimum
     */
    NONE(1.2),
    /**
     * For people with low physical activity (1-3 workouts per week)
     */
    LOW(1.375),
    /**
     * For moderately active people (work of moderate severity or moderate efficiency 3 - 5 days a week)
     */
    MODERATE(1.55),
    /**
     * For active people (physical work + training / active training 6-7 times a week)
     */
    ACTIVE(1.725),
    /**
     * For extremely active people (physical work + very intense workouts / sports activities)
     */
    EXTREMELY_ACTIVE(1.9);

    private double value;

    PhysicalActivity(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
