package ru.akvine.fitstats.entities.security;

public interface OneTimePasswordable {
    Long getId();
    String getLogin();
    OtpActionEntity getOtpAction();
    String getSessionId();
}
