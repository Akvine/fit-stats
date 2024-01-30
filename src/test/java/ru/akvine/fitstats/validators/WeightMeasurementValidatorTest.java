package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.WeightMeasurement;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("WeightMeasurementValidator Test")
public class WeightMeasurementValidatorTest {
    private final static WeightMeasurementValidator validator = new WeightMeasurementValidator();

    @Test
    @DisplayName("FAIL - weight is blank")
    public void test_validate_weight_is_blank() {
        String weight = null;
        assertThatThrownBy(() -> validator.validate(weight))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Weight measurement is blank. Field name: weightMeasurement");
    }

    @Test
    @DisplayName("FAIL - weight is invalid")
    public void test_validate_weight_is_invalid() {
        String weight = "invalid weight";
        assertThatThrownBy(() -> validator.validate(weight))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Weight measurement is invalid. Field name: weightMeasurement");
    }

    @Test
    @DisplayName("SUCCESS - valid weight")
    public void test_validate_weight_valid() {
        String weight = WeightMeasurement.KG.name();
        validator.validate(weight);
    }
}
