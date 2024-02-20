package ru.akvine.fitstats.exceptions.client;

public class ClientSettingsNotFoundException extends RuntimeException {
    public ClientSettingsNotFoundException(String message) {
        super(message);
    }
}
