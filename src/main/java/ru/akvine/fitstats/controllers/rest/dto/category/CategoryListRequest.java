package ru.akvine.fitstats.controllers.rest.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryListRequest {
    private String filter;
}
