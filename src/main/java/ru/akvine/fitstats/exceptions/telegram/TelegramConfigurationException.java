package ru.akvine.fitstats.exceptions.telegram;

public class TelegramConfigurationException extends RuntimeException {
    public TelegramConfigurationException(String message) {
        super(message);
    }
}
