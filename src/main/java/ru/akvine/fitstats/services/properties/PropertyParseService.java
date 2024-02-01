package ru.akvine.fitstats.services.properties;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyParseService {
    private final PropertyService propertyService;

    public String get(String name) {
        return propertyService.get(name);
    }

    public Integer parseInteger(String name) {
        return Integer.valueOf(propertyService.getRequired(name));
    }

    public Long parseLong(String name) {
        return Long.valueOf(propertyService.getRequired(name));
    }

    public Double parseDouble(String name) {
        return Double.parseDouble(propertyService.get(name));
    }

    public Boolean parseBoolean(String name) {
        return Boolean.parseBoolean(propertyService.get(name));
    }

    public char parseChar(String name) {
        return propertyService.get(name).charAt(0);
    }

    public List<String> parseToList(String name) {
        String propertyValue = propertyService.get(name);
        if (StringUtils.isBlank(propertyValue)) {
            return new ArrayList<>();
        }

        return Arrays.stream(propertyValue.split(","))
                .collect(Collectors.toList());
    }
}
