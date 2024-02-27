package ru.akvine.fitstats.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringHelper {
    public static List<String> splitter(String filter, List<String> delimiters) {
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
        result.removeIf(org.apache.commons.lang3.StringUtils::isBlank);
        return result;
    }
}
