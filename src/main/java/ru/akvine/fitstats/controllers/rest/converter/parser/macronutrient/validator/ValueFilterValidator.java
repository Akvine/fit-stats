package ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.validator;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValueFilterValidator implements MacronutrentFilterValidator {
    private final static int VALUE_START_POSITION = 2;

    @Override
    public void validate(List<String> splitted) {
        for (int i = VALUE_START_POSITION; i < splitted.size(); i += 4) {
            double value;
            try {
                value = Double.parseDouble(splitted.get(i));
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException(PARSE_EXCEPTION_MESSAGE_START + "values position invalid");
            }

            if (value < 0) {
                throw new IllegalArgumentException(PARSE_EXCEPTION_MESSAGE_START + "values must be more or equals 0");
            }
        }
    }
}
