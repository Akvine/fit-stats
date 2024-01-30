package ru.akvine.fitstats.enums;

public enum StatisticType {
    AVG,
    MAX,
    SUM,
    MEDIAN;

    public static StatisticType safeValueOf(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Statistic type value is null");
        }

        switch (value.toLowerCase()) {
            case "avg": return AVG;
            case "max": return MAX;
            case "sum": return SUM;
            case "median": return MEDIAN;
            default: throw new IllegalStateException("Statistic type with value = [" + value + "] is not supported!");
        }
    }
}
