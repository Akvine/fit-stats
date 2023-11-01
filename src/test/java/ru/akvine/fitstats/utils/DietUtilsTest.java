package ru.akvine.fitstats.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.enums.HeightMeasurement;
import ru.akvine.fitstats.enums.WeightMeasurement;
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

        Macronutrients result = DietUtils.transformPer100(proteins, fats, carbohydrates, volume);
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

        Macronutrients result = DietUtils.transformPer100(proteins, fats, carbohydrates, volume);
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
        double result = DietUtils.calculateCalories(proteins, fats, carbohydrates);
        double expected = 105;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Calculate calories - case 2")
    void calculate_calories_case2() {
        double proteins = 100;
        double fats = 35;
        double carbohydrates = 45;
        double result = DietUtils.calculateCalories(proteins, fats, carbohydrates);
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

    @Test
    @DisplayName("Convert to kg - KG")
    void convert_to_kg() {
        double weight = 50;
        double result = DietUtils.convertToKg(weight, WeightMeasurement.KG);
        double expected = 50;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert to kg - IB")
    void convert_to_ib() {
        double weight = 50;
        double result = MathUtils.round(DietUtils.convertToKg(weight, WeightMeasurement.IB), 2);
        double expected = 22.68;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert to kg - OZ")
    void convert_to_oz() {
        double weight = 50;
        double result = MathUtils.round(DietUtils.convertToKg(weight, WeightMeasurement.OZ), 2);
        double expected = 1.42;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert to cm - CM")
    void convert_to_cm() {
        double height = 175;
        double result = DietUtils.convertToCm(height, HeightMeasurement.CM);
        double expected = 175;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert to cm - FT")
    void convert_to_ft() {
        double height = 175;
        double result = MathUtils.round(DietUtils.convertToCm(height, HeightMeasurement.FT), 2);
        double expected = 5334;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert to cm - IN")
    void convert_to_in() {
        double height = 175;
        double result = MathUtils.round(DietUtils.convertToCm(height, HeightMeasurement.IN), 2);
        double expected = 444.5;

        assertThat(result).isEqualTo(expected);
    }
}