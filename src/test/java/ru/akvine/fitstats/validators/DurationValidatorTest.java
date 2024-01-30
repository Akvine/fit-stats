package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("DurationValidator Test")
public class DurationValidatorTest {
    private final static DurationValidator validator = new DurationValidator();

    @Test
    @DisplayName("FAIL - duration is blank")
    public void test_validate_duration_is_blank() {
        String duration = null;
        assertThatThrownBy(() -> validator.validate(duration))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Duration is blank. Field name: duration");
    }

    @Test
    @DisplayName("FAIL - duration is invalid")
    public void test_validate_duration_is_invalid() {
        String duration = "invalid duration";
        assertThatThrownBy(() -> validator.validate(duration))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Duration is invalid. Field name: duration");
    }

    @Test
    @DisplayName("SUCCESS - duration is valid")
    public void test_validate_duration_is_valid() {
        String email = Duration.YEAR.name();
        validator.validate(email);
    }
}
