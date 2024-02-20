package ru.akvine.fitstats.managers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.converter.parser.Parser;
import ru.akvine.fitstats.enums.ConverterType;

import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ParsersManager {
    private final Map<ConverterType, Parser> parsers;
}
