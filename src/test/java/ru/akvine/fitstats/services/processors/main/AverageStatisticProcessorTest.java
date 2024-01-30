package ru.akvine.fitstats.services.processors.main;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.services.processors.statistic.main.AverageStatisticProcessor;
import ru.akvine.fitstats.utils.MathUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("AverageStatisticProcessor Test")
public class AverageStatisticProcessorTest {
    private final static AverageStatisticProcessor processor = new AverageStatisticProcessor();

    @DisplayName("Case 1")
    @Test
    public void case1() {
        List<Double> values = List.of(1D, 2D, 3D, 4D, 5D);
        double expected = 3;

        double result = processor.calculate(values);
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Case 2")
    @Test
    public void case2() {
        List<Double> values = List.of(-1D, -2D, -9.3D, 14D, 35D);
        double expected = 7.34;

        double result = MathUtils.round(processor.calculate(values), 2);
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Case 3")
    @Test
    public void case3() {
        List<Double> values = List.of(0D, 0D, 0D, 0D, 0D);
        double expected = 0;

        double result = processor.calculate(values);
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Case 4")
    @Test
    public void case4() {
        List<Double> values = List.of(0D, -1D);
        double expected = -0.5;

        double result = MathUtils.round(processor.calculate(values), 1);
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Case 5")
    @Test
    public void case5() {
        List<Double> values = List.of(-0.521D, 3.413D, 213D, -39D, -484.1453D);
        double expected = -61.451;

        double result = MathUtils.round(processor.calculate(values), 3);
        assertThat(result).isEqualTo(expected);
    }
}
