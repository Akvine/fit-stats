package ru.akvine.fitstats.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.services.dto.DateRange;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("DateUtils Test")
public class DateUtilsTest {

    @DisplayName("Get day range")
    @Test
    public void get_day_range() {
        DateRange dateRange = DateUtils.getDayRange();

        assertThat(dateRange).isNotNull();
        assertThat(dateRange.getStartDate()).isNotNull();
        assertThat(dateRange.getEndDate()).isNotNull();
    }

    @DisplayName("Get week range")
    @Test
    public void get_week_range() {
        DateRange dateRange = DateUtils.getWeekRange();

        assertThat(dateRange).isNotNull();
        assertThat(dateRange.getStartDate()).isNotNull();
        assertThat(dateRange.getEndDate()).isNotNull();
    }

    @DisplayName("Get month range")
    @Test
    public void get_month_range() {
        DateRange dateRange = DateUtils.getMonthRange();

        assertThat(dateRange).isNotNull();
        assertThat(dateRange.getStartDate()).isNotNull();
        assertThat(dateRange.getEndDate()).isNotNull();
    }

    @DisplayName("Get year range")
    @Test
    public void get_year_range() {
        DateRange dateRange = DateUtils.getYearRange();

        assertThat(dateRange).isNotNull();
        assertThat(dateRange.getStartDate()).isNotNull();
        assertThat(dateRange.getEndDate()).isNotNull();
    }

    @DisplayName("Local date is null")
    @Test
    public void local_date_is_null() {
        String date = null;

        assertThatThrownBy(() -> DateUtils.convertToLocalDate(date))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("date can't be blank!");
    }

    @DisplayName("Invalid date")
    @Test
    public void invalid_date() {
        String date = "invalid date";

        assertThatThrownBy(() -> DateUtils.convertToLocalDate(date))
                .isInstanceOf(DateTimeParseException.class)
                .hasMessage("Text 'invalid date' could not be parsed at index 0");
    }

    @DisplayName("Convert to local date by default formatter")
    @Test
    public void convert_to_local_date_by_default_formatter() {
        String date = "2024-01-01";
        LocalDate expected = LocalDate.of(2024, 1, 1);

        LocalDate result = DateUtils.convertToLocalDate(date);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Time is null")
    @Test
    public void time_is_null() {
        String date = null;

        assertThatThrownBy(() -> DateUtils.convertToLocalTime(date))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("time can't be blank!");
    }

    @DisplayName("Invalid time")
    @Test
    public void invalid_time() {
        String date = "invalid time";

        assertThatThrownBy(() -> DateUtils.convertToLocalTime(date))
                .isInstanceOf(DateTimeParseException.class)
                .hasMessage("Text 'invalid time' could not be parsed at index 0");
    }

    @DisplayName("Convert to time by default formatter")
    @Test
    public void convert_to_time_by_default_formatter() {
        String date = "01:00:00";
        LocalTime expected = LocalTime.of(1, 0, 0);

        LocalTime result = DateUtils.convertToLocalTime(date);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);
    }
}
