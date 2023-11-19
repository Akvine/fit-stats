package ru.akvine.fitstats.exceptions.security.registration;

public class RegistrationNotStartedException extends RuntimeException {
    public RegistrationNotStartedException(String message) {
        super(message);
    }
}
