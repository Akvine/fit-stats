package ru.akvine.fitstats.services.properties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.exceptions.property.PropertyNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfigPropertyService implements PropertyService {
    private final Environment environment;

    @Override
    public String getRequired(@NotNull String name) {
        boolean isContains = environment.containsProperty(name);
        if (!isContains) {
            throw new PropertyNotFoundException("Property with name = [" + name + "] not found!");
        }
        return environment.getProperty(name);
    }

    @Override
    @Nullable
    public String get(@NotNull String name) {
        boolean isContains = environment.containsProperty(name);
        if (!isContains) {
            return null;
        }
        return environment.getProperty(name);
    }

    @Override
    public Map<String, String> get(List<String> names) {
        logger.info("Get properties with names = {}", names);
        Map<String, String> properties = new HashMap<>();
        names.forEach(key -> properties.put(key, get(key)));
        return properties;
    }
}
