package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.VolumeMeasurement;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("VolumeMeasurementValidator Test")
public class VolumeMeasurementValidatorTest {
    private final static VolumeMeasurementValidator validator = new VolumeMeasurementValidator();

    @Test
    @DisplayName("FAIL - volume is blank")
    public void test_validate_volume_is_blank() {
        String volume = null;
        assertThatThrownBy(() -> validator.validate(volume))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Volume measurement value is blank. Field name: volumeMeasurement");
    }

    @Test
    @DisplayName("FAIL - volume is invalid")
    public void test_validate_volume_is_invalid() {
        String volume = "invalid volume";
        assertThatThrownBy(() -> validator.validate(volume))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Volume measurement = [invalid volume] is not supported!");
    }

    @Test
    @DisplayName("SUCCESS - valid volume")
    public void test_validate_volume_valid() {
        String volume = VolumeMeasurement.GRAMS.name();
        validator.validate(volume);
    }
}
