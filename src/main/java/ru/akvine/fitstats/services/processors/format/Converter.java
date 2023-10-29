package ru.akvine.fitstats.services.processors.format;

import ru.akvine.fitstats.enums.ConverterType;

import java.util.List;

public interface Converter {
    <T> byte[] convert(List<T> records, Class<T> clazz);
    ConverterType getType();
}
