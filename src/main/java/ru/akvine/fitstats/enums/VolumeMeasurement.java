package ru.akvine.fitstats.enums;

import org.apache.commons.lang3.StringUtils;

public enum VolumeMeasurement {
    GRAMS,
    MILLILITERS;

    public static VolumeMeasurement safeValueOf(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Volume measurement value is blank. Field name: volumeMeasurement");
        }

        switch (value.toUpperCase()) {
            case "GRAMS":
            case "G":
                return GRAMS;
            case "MILLILITERS":
            case "M":
                return MILLILITERS;
            default:
                throw new IllegalArgumentException("Volume measurement = [" + value + "] is not supported!");
        }
    }
}
