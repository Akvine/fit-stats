package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailValidator Test")
public class EmailValidatorTest {
    private final static EmailValidator validator = new EmailValidator();

    @Test
    @DisplayName("FAIL - email is blank")
    public void test_validate_email_is_blank() {
        String email = null;
        assertThatThrownBy(() -> validator.validate(email))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Email is blank. Field name: email");
    }

    @Test
    @DisplayName("FAIL - email is invalid")
    public void test_validate_email_is_invalid() {
        String email = "invalid email";
        assertThatThrownBy(() -> validator.validate(email))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Email is invalid. Field name: email");
    }

    @Test
    @DisplayName("SUCCESS - email is valid")
    public void test_validate_email_is_valid() {
        String email = "testEmail@mail.com";
        validator.validate(email);
    }
}
