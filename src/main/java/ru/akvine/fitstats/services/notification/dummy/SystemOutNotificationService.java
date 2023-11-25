package ru.akvine.fitstats.services.notification.dummy;

import org.springframework.stereotype.Service;
import ru.akvine.fitstats.enums.NotificationProviderType;

@Service
public class SystemOutNotificationService implements DummyNotificationProvider {
    @Override
    public boolean sendRegistrationCode(String login, String code) {
        String message = String.format("Successful send registration code = [%s] to login = [%s]", code, login);
        System.out.println(message);
        return true;
    }

    @Override
    public boolean sendAuthenticationCode(String login, String code) {
        String message = String.format("Successful send auth code = [%s] to login = [%s]", code, login);
        System.out.println(message);
        return true;
    }

    @Override
    public boolean sendAccessRestoreCode(String login, String code) {
        String message = String.format("Successful send access restore code = [%s] to login = [%s]", code, login);
        System.out.println(message);
        return true;
    }

    @Override
    public NotificationProviderType getType() {
        return NotificationProviderType.SYSTEM_OUT;
    }
}
