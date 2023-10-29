package ru.akvine.fitstats.validators;

public interface Validator<T> {
    void validate(T object);
}
