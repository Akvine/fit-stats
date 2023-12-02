package ru.akvine.fitstats.exceptions.telegram;

public class TelegramSubscriptionNotFoundException extends RuntimeException {
    public TelegramSubscriptionNotFoundException(String message) {
        super(message);
    }
}
