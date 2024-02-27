package ru.akvine.fitstats.utils;

public final class MathUtils {
    public static double round(double value, int roundAccuracy) {
        return Math.round(value * Math.pow(10, roundAccuracy)) / Math.pow(10, roundAccuracy);
    }

    public static int round(double value) {
        return (int) Math.round(value);
    }
}
