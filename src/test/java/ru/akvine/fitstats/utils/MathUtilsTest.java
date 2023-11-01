package ru.akvine.fitstats.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Math Utils Test")
class MathUtilsTest {

    @Test
    @DisplayName("Test round to whole - case 1")
    void test_round_to_whole_case1() {
        double value = 10.4;
        int result = MathUtils.round(value);
        int expected = 10;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test round to whole - case 2")
    void test_round_to_whole_case2() {
        double value = 10.5;
        int result = MathUtils.round(value);
        int expected = 11;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test round to whole - case 3")
    void test_round_to_whole_case3() {
        double value = 10.7;
        int result = MathUtils.round(value);
        int expected = 11;

        assertThat(result).isEqualTo(expected);
    }
}