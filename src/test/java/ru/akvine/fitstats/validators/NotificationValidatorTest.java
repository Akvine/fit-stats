package ru.akvine.fitstats.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akvine.fitstats.enums.NotificationProviderType;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationValidator Test")
public class NotificationValidatorTest {
    private final static NotificationValidator validator = new NotificationValidator();

    @Test
    @DisplayName("FAIL - physical activity is blank")
    public void test_validate_notification_is_blank() {
        String notificationType = null;
        assertThatThrownBy(() -> validator.validate(notificationType))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Notification provider type is blank");
    }

    @Test
    @DisplayName("FAIL - physical activity is invalid")
    public void test_validate_notification_is_invalid() {
        String notificationType = "invalid notification type";
        assertThatThrownBy(() -> validator.validate(notificationType))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Notification provider with type = [invalid notification type] not supported!");
    }

    @Test
    @DisplayName("SUCCESS - valid physical activity")
    public void test_validate_notification_valid() {
        String notificationType = NotificationProviderType.SYSTEM_OUT.name();
        validator.validate(notificationType);
    }
}
