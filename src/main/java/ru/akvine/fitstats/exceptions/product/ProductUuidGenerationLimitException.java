package ru.akvine.fitstats.exceptions.product;

public class ProductUuidGenerationLimitException extends RuntimeException {
    public ProductUuidGenerationLimitException(String message) {
        super(message);
    }
}
