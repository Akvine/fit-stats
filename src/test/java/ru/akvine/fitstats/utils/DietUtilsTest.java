package ru.akvine.fitstats.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.services.dto.Macronutrients;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Diet Utils Test")
class DietUtilsTest {

    @Test
    @DisplayName("Transform per 100 - case 1")
    void transform_per_100_case1() {
        double volume = 100;
        double proteins = 5;
        double fats = 5;
        double carbohydrates = 10;

        Macronutrients result = DietUtils.transformPer100(proteins, fats, carbohydrates, 0, volume);
        assertThat(result).isNotNull();
        assertThat(result.getProteins()).isEqualTo(proteins);
        assertThat(result.getFats()).isEqualTo(fats);
        assertThat(result.getCarbohydrates()).isEqualTo(carbohydrates);;
    }

    @Test
    @DisplayName("Transform per 100 - case 2")
    void transform_per_100_case2() {
        double volume = 150;
        double proteins = 5;
        double fats = 5;
        double carbohydrates = 10;

        Macronutrients result = DietUtils.transformPer100(proteins, fats, carbohydrates, 0, volume);
        assertThat(result).isNotNull();
        assertThat(MathUtils.round(result.getProteins(), 2)).isEqualTo(3.33);
        assertThat(MathUtils.round(result.getFats(), 2)).isEqualTo(3.33);
        assertThat(MathUtils.round(result.getCarbohydrates(), 2)).isEqualTo(6.67);
    }

    @Test
    @DisplayName("Calculate calories - case 1")
    void calculate_calories_case1() {
        double proteins = 5;
        double fats = 5;
        double carbohydrates = 10;
        double result = DietUtils.calculateCalories(proteins, fats, carbohydrates, 0);
        double expected = 105;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Calculate calories - case 2")
    void calculate_calories_case2() {
        double proteins = 100;
        double fats = 35;
        double carbohydrates = 45;
        double result = DietUtils.calculateCalories(proteins, fats, carbohydrates, 0);
        double expected = 895;

        assertThat(result).isEqualTo(expected);
    }

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