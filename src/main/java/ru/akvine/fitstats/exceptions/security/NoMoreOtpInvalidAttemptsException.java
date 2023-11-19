package ru.akvine.fitstats.exceptions.security;

public class NoMoreOtpInvalidAttemptsException extends RuntimeException {
    public NoMoreOtpInvalidAttemptsException(String message) {
        super(message);
    }
}
