package ru.akvine.fitstats.exceptions.barcode;

public class BarCodeNotFoundException extends RuntimeException {
    public BarCodeNotFoundException(String message) {
        super(message);
    }
}
