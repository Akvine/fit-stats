package ru.akvine.fitstats.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.Gender;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Diet Utils Test")
class DietUtilsTest {

    @Test
    @DisplayName("Calculate basic exchange - MALE")
    void calculate_basic_exchange_male() {
        double weight = 70;
        double height = 180;
        int age = 32;
        Gender gender = Gender.MALE;
        double result = DietUtils.calculateBasicExchange(gender, age, height, weight);
        double expected = 1670;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Calculate basic exchange - FEMALE")
    void calculate_basic_exchange_female() {
        double weight = 33;
        double height = 210;
        int age = 45;
        Gender gender = Gender.FEMALE;
        double result = DietUtils.calculateBasicExchange(gender, age, height, weight);
        double expected = 1256.5;

        assertThat(result).isEqualTo(expected);
    }
}