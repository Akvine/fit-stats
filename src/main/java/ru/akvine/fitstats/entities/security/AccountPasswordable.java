package ru.akvine.fitstats.entities.security;

public interface AccountPasswordable {
    int decrementPwdInvalidAttemptsLeft();
    int getPwdInvalidAttemptsLeft();
}
