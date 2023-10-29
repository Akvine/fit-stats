package ru.akvine.fitstats.utils;

public class MathUtils {
    public static double round(double value, int decimalPlaces) {
        return Math.round(value * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
    }

    public static int round(double value) {
        return (int) Math.round(value);
    }
}
