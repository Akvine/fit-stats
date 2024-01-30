package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("PhysicalActivitiesValidator Test")
public class PhysicalActivitiesValidatorTest {
    private final static PhysicalActivitiesValidator validator = new PhysicalActivitiesValidator();

    @Test
    @DisplayName("FAIL - physical activity is blank")
    public void test_validate_physical_activity_is_blank() {
        String physicalActivity = null;
        assertThatThrownBy(() -> validator.validate(physicalActivity))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Physical activity is blank. Field name: physicalActivity");
    }

    @Test
    @DisplayName("FAIL - physical activity is invalid")
    public void test_validate_physical_activity_is_invalid() {
        String physicalActivity = "invalid physical activity";
        assertThatThrownBy(() -> validator.validate(physicalActivity))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Physical activity is invalid. Field name: physicalActivity");
    }

    @Test
    @DisplayName("SUCCESS - valid physical activity")
    public void test_validate_physical_activity_valid() {
        String physicalActivity = PhysicalActivity.EXTREMELY_ACTIVE.name();
        validator.validate(physicalActivity);
    }
}
