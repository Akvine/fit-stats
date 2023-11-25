package ru.akvine.fitstats.services.notification;

import ru.akvine.fitstats.enums.NotificationProviderType;

public interface NotificationProvider {
    boolean sendRegistrationCode(String login, String code);
    boolean sendAuthenticationCode(String login, String code);
    boolean sendAccessRestoreCode(String login, String code);
    boolean sendProfileDeleteCode(String login, String code);
    boolean sendProfileChangeEmailCode(String login, String code);

    NotificationProviderType getType();
}
