package ru.akvine.fitstats.exceptions.telegram;

public class TelegramSubscriptionTypeNotFoundException extends RuntimeException {
    public TelegramSubscriptionTypeNotFoundException(String message) {
        super(message);
    }
}
