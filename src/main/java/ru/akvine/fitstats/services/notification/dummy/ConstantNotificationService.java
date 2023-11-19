package ru.akvine.fitstats.services.notification.dummy;

import org.springframework.stereotype.Service;
import ru.akvine.fitstats.enums.NotificationProviderType;

@Service
public class ConstantNotificationService implements DummyNotificationProvider {
    @Override
    public boolean sendRegistrationCode(String login, String code) {
        return true;
    }

    @Override
    public boolean sendAuthenticationCode(String login, String code) {
        return true;
    }

    @Override
    public NotificationProviderType getType() {
        return NotificationProviderType.CONSTANT;
    }
}
