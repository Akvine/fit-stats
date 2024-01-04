package ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.validator;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MacronutrientFilterValidator implements MacronutrentFilterValidator {
    private final static List<String> macronutrients = List.of("fats", "proteins", "carbohydrates");
    private final static int MACRONUTRIENT_START_POSITION = 0;

    @Override
    public void validate(List<String> splitted) {
        for (int i = MACRONUTRIENT_START_POSITION; i < splitted.size(); i += 4) {
            if (!macronutrients.contains(splitted.get(i))) {
                throw new IllegalArgumentException(PARSE_EXCEPTION_MESSAGE_START + "macronutrient position invalid");
            }
        }
    }
}
