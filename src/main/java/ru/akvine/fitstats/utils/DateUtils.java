package ru.akvine.fitstats.utils;

import org.apache.commons.lang3.StringUtils;
import ru.akvine.fitstats.services.dto.DateRange;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER_DEFAULT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER_DEFAULT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final int HALF_YEAR_MONTH_COUNT = 6;

    public static LocalTime convertToLocalTime(@NotNull String time) {
        return convertToLocalTime(time, TIME_FORMATTER_DEFAULT);
    }

    public static LocalTime convertToLocalTime(@NotNull String time, DateTimeFormatter dateTimeFormatter) {
        if (StringUtils.isBlank(time)) {
            throw new IllegalArgumentException("time can't be blank!");
        }
        return LocalTime.from(dateTimeFormatter.parse(time));
    }


    public static LocalDate convertToLocalDate(@NotNull String date) {
        return convertToLocalDate(date, DATE_FORMATTER_DEFAULT);
    }

    public static LocalDate convertToLocalDate(@NotNull String date, DateTimeFormatter dateTimeFormatter) {
        if (StringUtils.isBlank(date)) {
            throw new IllegalArgumentException("date can't be blank!");
        }
        return LocalDate.from(dateTimeFormatter.parse(date));
    }

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

    public static DateRange getPastDaysInMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate start = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = currentDate.minusDays(1);
        return new DateRange(start, end);
    }

    public static DateRange getPastWeeksInMonths() {
        return getPastWeeksInMonths(HALF_YEAR_MONTH_COUNT);
    }
    public static DateRange getPastWeeksInMonths(int monthCount) {
        LocalDate currentDate = LocalDate.now();
        LocalDate start = currentDate.minusMonths(monthCount).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = currentDate.minusDays(1);
        return new DateRange(start, end);
    }

    public static DateRange getPastMonthsInYear() {
        LocalDate currentDate = LocalDate.now();
        LocalDate start = currentDate.minusYears(1).with(TemporalAdjusters.firstDayOfYear());
        LocalDate end = currentDate.minusDays(1);
        return new DateRange(start, end);
    }

    public static DateRange getPastYears() {
        LocalDate currentDate = LocalDate.now();
        LocalDate start = currentDate.minusYears(5).with(TemporalAdjusters.firstDayOfYear());
        LocalDate end = currentDate.minusDays(1);
        return new DateRange(start, end);
    }
}
