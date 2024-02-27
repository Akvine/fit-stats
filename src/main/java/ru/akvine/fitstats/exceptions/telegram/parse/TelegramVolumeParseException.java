package ru.akvine.fitstats.exceptions.telegram.parse;

public class TelegramVolumeParseException extends RuntimeException {
    public TelegramVolumeParseException(String message) {
        super(message);
    }
}
