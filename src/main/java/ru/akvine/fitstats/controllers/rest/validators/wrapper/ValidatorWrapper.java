package ru.akvine.fitstats.controllers.rest.validators.wrapper;

public interface ValidatorWrapper<T, R> {
    R wrap(T obj);
}
