package ru.akvine.fitstats.enums;

public enum ConverterType {
    CSV("csv");

    private final String value;

    ConverterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
