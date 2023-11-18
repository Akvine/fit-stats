package ru.akvine.fitstats.controllers.rest.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class ListProductRequest {
    private String filter;
    private Map<String, Double> macronutrients;
}
