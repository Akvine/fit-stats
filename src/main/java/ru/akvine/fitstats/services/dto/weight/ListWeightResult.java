package ru.akvine.fitstats.services.dto.weight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ListWeightResult {
    private Map<String, String> weight;
}
