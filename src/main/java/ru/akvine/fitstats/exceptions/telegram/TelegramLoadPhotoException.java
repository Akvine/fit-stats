package ru.akvine.fitstats.exceptions.telegram;

public class TelegramLoadPhotoException extends RuntimeException {
    public TelegramLoadPhotoException(String message) {
        super(message);
    }
}
