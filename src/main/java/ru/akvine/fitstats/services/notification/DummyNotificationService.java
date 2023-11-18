package ru.akvine.fitstats.services.notification;

import org.springframework.stereotype.Service;

@Service
public class DummyNotificationService implements NotificationProvider {
    @Override
    public boolean sendRegistrationCode(String login, String code) {
        return true;
    }

    @Override
    public boolean sendAuthenticationCode(String login, String code) {
        return true;
    }
}
