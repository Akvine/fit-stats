package ru.akvine.fitstats.controllers.rest.converter.parser;

import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.enums.ConverterType;

import java.util.List;

public interface Parser {
    List<?> parse(MultipartFile file, Class clazz);

    ConverterType getType();
}
