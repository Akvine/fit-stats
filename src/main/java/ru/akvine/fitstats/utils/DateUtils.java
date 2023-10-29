package ru.akvine.fitstats.utils;

import ru.akvine.fitstats.services.dto.DateRange;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
    public static DateRange getDayRange() {
        LocalDate today = LocalDate.now();
        return new DateRange(today, today);
    }

    public static DateRange getWeekRange() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        return new DateRange(startOfWeek, endOfWeek);
    }

    public static DateRange getMonthRange() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        return new DateRange(firstDayOfMonth, lastDayOfMonth);
    }

    public static DateRange getYearRange() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfYear = today.with(TemporalAdjusters.firstDayOfYear());
        LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
        return new DateRange(firstDayOfYear, lastDayOfYear);
    }
}
