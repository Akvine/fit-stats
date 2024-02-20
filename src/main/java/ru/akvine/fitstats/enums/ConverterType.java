package ru.akvine.fitstats.enums;

public enum ConverterType {
    CSV("csv"),
    XLSX("xlsx");

    private final String value;

    ConverterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
