package ru.akvine.fitstats.exceptions.security;

public class OtpAuthRequiredException extends RuntimeException {
    public OtpAuthRequiredException(String message) {
        super(message);
    }
}
