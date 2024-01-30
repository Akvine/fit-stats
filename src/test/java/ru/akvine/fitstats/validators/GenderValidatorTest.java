package ru.akvine.fitstats.validators;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("GenderValidator Test")
public class GenderValidatorTest {
    private final static GenderValidator validator = new GenderValidator();

    @Test
    @DisplayName("FAIL - gender is blank")
    public void test_validate_gender_is_blank() {
        String gender = null;
        assertThatThrownBy(() -> validator.validate(gender))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Gender is blank. Field name: gender");
    }

    @Test
    @DisplayName("FAIL - gender is invalid")
    public void test_validate_gender_is_invalid() {
        String gender = "invalid gender";
        assertThatThrownBy(() -> validator.validate(gender))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Gender is invalid. Field name: gender");
    }

    @Test
    @DisplayName("SUCCESS - gender is valid")
    public void test_validate_gender_is_valid() {
        String gender = Gender.FEMALE.name();
        validator.validate(gender);
    }
}
