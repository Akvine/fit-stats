package ru.akvine.fitstats.exceptions.barcode;

public class BarCodeAlreadyExistsException extends RuntimeException {
    public BarCodeAlreadyExistsException(String message) {
        super(message);
    }
}
