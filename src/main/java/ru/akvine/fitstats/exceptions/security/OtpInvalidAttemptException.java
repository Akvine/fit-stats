package ru.akvine.fitstats.exceptions.security;

public class OtpInvalidAttemptException extends RuntimeException {
    private String login;
    private int attemptsLeft;

    public OtpInvalidAttemptException(String login, int attemptsLeft) {
        this.login = login;
        this.attemptsLeft = attemptsLeft;
    }

    public String getLogin() {
        return login;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }
}
