package ru.akvine.fitstats.exceptions.weight;

public class WeightRecordNotFoundException extends RuntimeException {
    public WeightRecordNotFoundException(String message) {
        super(message);
    }
}
