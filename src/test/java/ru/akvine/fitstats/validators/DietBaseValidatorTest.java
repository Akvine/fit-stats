package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("DietBaseValidator Test")
public class DietBaseValidatorTest {
    private final static DietBaseValidator validator = new DietBaseValidator();

    @Test
    @DisplayName("FAIL - diet is blank")
    public void test_validate_diet_is_blank() {
        String diet = null;
        assertThatThrownBy(() -> validator.validate(diet))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Diet is blank. Field name: diet");
    }

    @Test
    @DisplayName("FAIL - diet is invalid")
    public void test_validate_duration_is_invalid() {
        String diet = "invalid diet";
        assertThatThrownBy(() -> validator.validate(diet))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Diet is invalid. Field name: diet");
    }

    @Test
    @DisplayName("SUCCESS - diet is valid")
    public void test_validate_duration_is_valid() {
        String email = Diet.DRYING.name();
        validator.validate(email);
    }
}
