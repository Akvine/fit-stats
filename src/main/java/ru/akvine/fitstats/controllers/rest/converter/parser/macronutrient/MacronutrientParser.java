package ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MacronutrientParser {
    private final static String PARSE_EXCEPTION_MESSAGE_START = "Illegal parse macronutrient filter: ";

    private final static List<String> macronutrients = List.of("fats", "proteins", "carbohydrates");
    private final static List<String> conditions = List.of("<", ">", "=");
    private final static List<String> logicConditions = List.of("AND", "OR");
    private final static List<String> delimiters = List.of("fats", "proteins", "carbohydrates", "<", ">", "=", "AND", "OR");

    public List<MacronutrientFilterPart> parse(String filter) {
        boolean isContainsMacronutrient = false;
        for (String macronutrient : macronutrients) {
            if (filter.contains(macronutrient)) {
                isContainsMacronutrient = true;
                break;
            }
        }

        boolean isContainsCondition = false;
        for (String condition : conditions) {
            if (filter.contains(condition)) {
                isContainsCondition = true;
                break;
            }
        }

        if (!(isContainsMacronutrient && isContainsCondition)) {
            throw new IllegalArgumentException(PARSE_EXCEPTION_MESSAGE_START + "filter must contains any macronutrient and condition");
        }

        List<String> splitted = splitter(filter, delimiters);

        validateMacronutrients(splitted);
        validateConditions(splitted);
        validateValues(splitted);
        validateLogicConditions(splitted);

        return createParts(splitted);
    }

    public List<String> splitter(String filter, String delimiter) {
        return splitter(filter, List.of(delimiter));
    }

    public List<String> splitter(String filter, List<String> delimiters) {
        String regex = "(" + String.join("|", delimiters.stream().map(Pattern::quote).toArray(String[]::new)) + ")";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(filter);
        List<String> result = new ArrayList<>();

        int start = 0;
        while (matcher.find()) {
            result.add(filter.substring(start, matcher.start()));
            result.add(matcher.group(1));
            start = matcher.end();
        }

        result.add(filter.substring(start));
        result.removeIf(StringUtils::isBlank);
        return result;
    }

    public void validateMacronutrients(List<String> splitted) {
        for (int i = 0; i < splitted.size(); i += 4) {
            if (!macronutrients.contains(splitted.get(i))) {
                throw new IllegalArgumentException(PARSE_EXCEPTION_MESSAGE_START + "macronutrient position invalid");
            }
        }
    }

    public void validateConditions(List<String> splitted) {
        for (int i = 1; i < splitted.size(); i += 4) {
            if (!conditions.contains(splitted.get(i))) {
                throw new IllegalArgumentException(PARSE_EXCEPTION_MESSAGE_START + "condition position invalid");
            }
        }
    }

    public void validateValues(List<String> splitted) {
        for (int i = 2; i < splitted.size(); i += 4) {
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

    public void validateLogicConditions(List<String> splitted) {
        for (int i = 3; i < splitted.size(); i += 4) {
            if (!logicConditions.contains(splitted.get(i))) {
                throw new IllegalArgumentException(PARSE_EXCEPTION_MESSAGE_START + "logic condition position invalid");
            }
        }
    }

    private List<MacronutrientFilterPart> createParts(List<String> splitted) {
        List<MacronutrientFilterPart> parts = new LinkedList<>();
        for (int i = 0; i < splitted.size(); i+= 4) {
            String macronutrient = splitted.get(i);
            String condition = splitted.get(i + 1);
            String value = splitted.get(i + 2);
            String logicCondition;
            try {
                logicCondition = splitted.get(i + 3);
            } catch (Exception exception) {
                logicCondition = null;
            }
            MacronutrientFilterPart part = new MacronutrientFilterPart(macronutrient, condition, value, logicCondition);
            parts.add(part);
        }
        return parts;
    }
}
