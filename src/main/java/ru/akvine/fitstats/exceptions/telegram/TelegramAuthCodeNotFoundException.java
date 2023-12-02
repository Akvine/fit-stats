package ru.akvine.fitstats.exceptions.telegram;

public class TelegramAuthCodeNotFoundException extends RuntimeException {
    public TelegramAuthCodeNotFoundException(String message) {
        super(message);
    }
}
