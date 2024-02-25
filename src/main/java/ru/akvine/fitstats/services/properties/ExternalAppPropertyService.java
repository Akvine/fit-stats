package ru.akvine.fitstats.services.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ru.akvine.fitstats.enums.PropertyLoadStrategyType;
import ru.akvine.fitstats.exceptions.property.PropertyNotFoundException;
import ru.akvine.fitstats.functional.Refreshable;
import ru.akvine.fitstats.services.integration.ConfigaIntegrationService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class ExternalAppPropertyService implements PropertyService, Refreshable {
    private final ConfigaIntegrationService configaIntegrationService;

    private Map<String, String> properties = new HashMap<>();

    @PostConstruct
    private void init() throws JsonProcessingException {
        properties = configaIntegrationService.listProperties();
    }

    @Override
    public String getRequired(@NotNull String name) {
        boolean isContains = properties.containsKey(name);
        if (!isContains) {
            throw new PropertyNotFoundException("Property with name = [" + name + "] not found!");
        }
        return properties.get(name);
    }

    @Override
    public String get(@NotNull String name) {
        boolean isContains = properties.containsKey(name);
        if (!isContains) {
            return null;
        }
        return properties.get(name);
    }

    @Override
    public Map<String, String> get(List<String> names) {
        Map<String, String> find = new HashMap<>();
        names.forEach(key -> find.put(key, get(key)));
        return find;
    }

    @Override
    public PropertyLoadStrategyType getType() {
        return PropertyLoadStrategyType.EXTERNAL_APP;
    }

    @Override
    public boolean refresh() {
        properties = configaIntegrationService.listProperties();
        return true;
    }
}
