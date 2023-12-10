package ru.akvine.fitstats.exceptions.telegram.parse;

public class TelegramParseException extends RuntimeException {
    public TelegramParseException(String message) {
        super(message);
    }
}
