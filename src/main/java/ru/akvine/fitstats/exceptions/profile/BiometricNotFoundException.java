package ru.akvine.fitstats.exceptions.profile;

public class BiometricNotFoundException extends RuntimeException {
    public BiometricNotFoundException(String message) {
        super(message);
    }
}
