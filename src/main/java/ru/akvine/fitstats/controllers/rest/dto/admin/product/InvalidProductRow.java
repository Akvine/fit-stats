package ru.akvine.fitstats.controllers.rest.dto.admin.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class InvalidProductRow {
    private String rowNumber;
    private Map<String, String> errors;
}
