package ru.akvine.fitstats.exceptions.security;

public class BlockedCredentialsException extends RuntimeException {
    public BlockedCredentialsException(String message) {
        super(message);
    }
}
