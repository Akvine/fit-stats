package ru.akvine.fitstats.exceptions.diet;

public class DietSettingNotFoundException extends RuntimeException {
    public DietSettingNotFoundException(String message) {
        super(message);
    }
}
