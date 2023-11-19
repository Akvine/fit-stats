package ru.akvine.fitstats.exceptions.security;

public class WrongSessionException extends RuntimeException {
    public WrongSessionException(String message) {
        super(message);
    }
}
