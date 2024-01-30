package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.Macronutrient;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("MacronutrientValidator Test")
public class MacronutrientValidatorTest {
    private final static MacronutrientValidator validator = new MacronutrientValidator();

    @Test
    @DisplayName("FAIL - macronutrient is blank")
    public void test_validate_macronutrient_is_blank() {
        String macronutrient = null;
        assertThatThrownBy(() -> validator.validate(macronutrient))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Macronutrient is blank. Field name: macronutrient");
    }

    @Test
    @DisplayName("FAIL - macronutrient is invalid")
    public void test_validate_macronutrient_is_invalid() {
        String macronutrient = "invalid macronutrient";
        assertThatThrownBy(() -> validator.validate(macronutrient))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Macronutrient is invalid. Field name: macronutrient");
    }

    @Test
    @DisplayName("SUCCESS - valid macronutrient")
    public void test_validate_macronutrient_valid() {
        String macronutrient = Macronutrient.FATS.name();
        validator.validate(macronutrient);
    }
}
