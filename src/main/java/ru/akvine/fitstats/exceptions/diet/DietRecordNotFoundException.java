package ru.akvine.fitstats.exceptions.diet;

public class DietRecordNotFoundException extends RuntimeException {
    public DietRecordNotFoundException(String message) {
        super(message);
    }
}
