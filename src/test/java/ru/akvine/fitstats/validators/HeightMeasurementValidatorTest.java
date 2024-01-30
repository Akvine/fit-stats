package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.HeightMeasurement;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("HeightMeasurementValidator Test")
public class HeightMeasurementValidatorTest {
    private final static HeightMeasurementValidator validator = new HeightMeasurementValidator();

    @Test
    @DisplayName("FAIL - height is blank")
    public void test_validate_height_is_blank() {
        String height = null;
        assertThatThrownBy(() -> validator.validate(height))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Height measurement is blank. Field name: heightMeasurement");
    }

    @Test
    @DisplayName("FAIL - height is invalid")
    public void test_validate_height_is_invalid() {
        String height = "invalid height";
        assertThatThrownBy(() -> validator.validate(height))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Height measurement is invalid. Field name: heightMeasurement");
    }

    @Test
    @DisplayName("SUCCESS - height is valid")
    public void test_validate_height_is_valid() {
        String height = HeightMeasurement.CM.name();
        validator.validate(height);
    }
}
