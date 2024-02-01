package ru.akvine.fitstats.services.properties;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface PropertyService {
    String getRequired(@NotNull String name);

    String get(@NotNull String name);

    Map<String, String> get(List<String> names);
}
