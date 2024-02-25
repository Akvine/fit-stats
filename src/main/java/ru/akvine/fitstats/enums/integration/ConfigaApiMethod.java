package ru.akvine.fitstats.enums.integration;

public enum ConfigaApiMethod {
    PROPERTIES_LIST("/properties/list");

    private final String value;

    ConfigaApiMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
