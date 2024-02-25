package ru.akvine.fitstats.exceptions.barcode;

public class BarCodeScanException extends RuntimeException {
    public BarCodeScanException(String message) {
        super(message);
    }
}
