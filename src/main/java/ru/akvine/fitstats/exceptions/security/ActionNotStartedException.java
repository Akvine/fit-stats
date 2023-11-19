package ru.akvine.fitstats.exceptions.security;

public class ActionNotStartedException extends RuntimeException {
    public ActionNotStartedException(String message) {
        super(message);
    }
}
