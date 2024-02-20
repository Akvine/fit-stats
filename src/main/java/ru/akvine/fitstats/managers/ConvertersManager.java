package ru.akvine.fitstats.managers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.services.processors.format.Converter;

import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ConvertersManager {
    private Map<ConverterType, Converter> converters;
}
