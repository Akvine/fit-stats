package ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.validator;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConditionFilterValidator implements MacronutrentFilterValidator {
    private final static List<String> conditions = List.of("<", ">", "=");
    private final static int CONDITION_START_POSITION = 1;

    @Override
    public void validate(List<String> splitted) {
        for (int i = CONDITION_START_POSITION; i < splitted.size(); i += 4) {
            if (!conditions.contains(splitted.get(i))) {
                throw new IllegalArgumentException(PARSE_EXCEPTION_MESSAGE_START + "condition position invalid");
            }
        }
    }
}
