package ru.akvine.fitstats.services.notification;

public interface NotificationProvider {
    boolean sendRegistrationCode(String login, String code);
    boolean sendAuthenticationCode(String login, String code);
}
