package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConverterTypeValidator Test")
public class ConverterTypeValidatorTest {
    private final static ConverterTypeValidator validator = new ConverterTypeValidator();

    @Test
    @DisplayName("FAIL - converter type is blank")
    public void test_validate_converter_type_is_blank() {
        String converterType = null;
        assertThatThrownBy(() -> validator.validate(converterType))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Converter type is blank. Field name: converterType");
    }

    @Test
    @DisplayName("FAIL - converter type is invalid")
    public void test_validate_converter_type_is_invalid() {
        String converterType = "invalid diet";
        assertThatThrownBy(() -> validator.validate(converterType))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Converter type is invalid. Field name: converterType");
    }

    @Test
    @DisplayName("SUCCESS - converter type is valid")
    public void test_validate_converter_type_is_valid() {
        String converterType = ConverterType.CSV.name();
        validator.validate(converterType);
    }
}
