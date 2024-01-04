package ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.validator.MacronutrentFilterValidator;
import ru.akvine.fitstats.utils.StringHelper;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MacronutrientParser {
    private final static List<String> delimiters = List.of("fats", "proteins", "carbohydrates", "<", ">", "=", "AND", "OR");
    private final List<MacronutrentFilterValidator> validators;

    private final static int CONDITION_START_POSITION = 1;
    private final static int VALUE_START_POSITION = 2;
    private final static int LOGIC_CONDITION_START_POSITION = 3;

    private final static String EMPTY_SPACE = " ";
    private final static String NON_EMPTY_SPACE = "";

    public List<MacronutrientFilterPart> parse(String filter) {
        String filterWithoutEmptySpace = filter.replaceAll(EMPTY_SPACE, NON_EMPTY_SPACE);
        List<String> splitted = StringHelper.splitter(filterWithoutEmptySpace, delimiters);
        validators.forEach(validator -> validator.validate(splitted));
        return createParts(splitted);
    }

    private List<MacronutrientFilterPart> createParts(List<String> splitted) {
        List<MacronutrientFilterPart> parts = new LinkedList<>();
        for (int i = 0; i < splitted.size(); i+= 4) {
            String macronutrient = splitted.get(i);
            String condition = splitted.get(i + CONDITION_START_POSITION);
            String value = splitted.get(i + VALUE_START_POSITION);

            if ((i + LOGIC_CONDITION_START_POSITION) == splitted.size()) {
                MacronutrientFilterPart part = new MacronutrientFilterPart(macronutrient, condition, value, null);
                parts.add(part);
            } else {
                String logicCondition = splitted.get(i + LOGIC_CONDITION_START_POSITION);
                MacronutrientFilterPart part = new MacronutrientFilterPart(macronutrient, condition, value, logicCondition);
                parts.add(part);
            }
        }
        return parts;
    }
}
