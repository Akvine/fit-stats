package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordValidator Test")
public class PasswordValidatorTest {
    private final static PasswordValidator validator = new PasswordValidator();

    @Test
    @DisplayName("FAIL - password is blank")
    public void test_validate_password_is_blank() {
        String password = null;
        assertThatThrownBy(() -> validator.validate(password))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Password is blank. Field name: password");
    }

    @Test
    @DisplayName("FAIL - password length less than min")
    public void test_validate_password_length_less_than_min() {
        String password = "123";
        assertThatThrownBy(() -> validator.validate(password))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Password is less than password min length = [8]. Field name: password");
    }

    @Test
    @DisplayName("FAIL - password length greater than max")
    public void test_validate_password_length_greater_than_max() {
        String password = "very very very long password length for this test";
        assertThatThrownBy(() -> validator.validate(password))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Password is greater than password max length = [20]. Field name: password");
    }

    @Test
    @DisplayName("FAIL - password contains empty space")
    public void test_validate_password_contains_empty_space() {
        String password = "dasd314 31i5C";
        assertThatThrownBy(() -> validator.validate(password))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Password contains empty spaces. Field name: password");
    }

    @Test
    @DisplayName("FAIL - password does not match the requirements")
    public void test_validate_password_does_not_math_requirements() {
        String password = "kk123vxa111f";
        assertThatThrownBy(() -> validator.validate(password))
                .isInstanceOf(ValidationException.class)
                .hasMessage("The password does not match the requirements. Field name: password");
    }

    @Test
    @DisplayName("FAIL - password is valid")
    public void test_validate_password_valid() {
        String password = "Kk123VXa111f";
        validator.validate(password);
    }
}
