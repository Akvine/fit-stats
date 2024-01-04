package ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.validator;

import java.util.List;

public interface MacronutrentFilterValidator {
    String PARSE_EXCEPTION_MESSAGE_START = "Illegal parse macronutrient filter: ";
    void validate(List<String> splitted);
}
