package ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.validator;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogicConditionFilterValidator implements MacronutrentFilterValidator {
    private final static List<String> logicConditions = List.of("AND", "OR");
    private final static int LOGIC_CONDITION_START_POSITION = 3;

    @Override
    public void validate(List<String> splitted) {
        for (int i = LOGIC_CONDITION_START_POSITION; i < splitted.size(); i += 4) {
            if (!logicConditions.contains(splitted.get(i))) {
                throw new IllegalArgumentException(PARSE_EXCEPTION_MESSAGE_START + "logic condition position invalid");
            }
        }
    }
}
