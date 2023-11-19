package ru.akvine.fitstats.services.notification.dummy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.enums.NotificationProviderType;

@Service
@Slf4j
public class LogNotificationService implements DummyNotificationProvider {
    @Override
    public boolean sendRegistrationCode(String login, String code) {
        logger.info("Successful send registration code = [" + code + "] to login = [" + login + "]");
        return true;
    }

    @Override
    public boolean sendAuthenticationCode(String login, String code) {
        logger.info("Successful send authentication code = [" + code + "] to login = [" + login + "]");
        return true;
    }

    @Override
    public NotificationProviderType getType() {
        return NotificationProviderType.LOG;
    }
}
