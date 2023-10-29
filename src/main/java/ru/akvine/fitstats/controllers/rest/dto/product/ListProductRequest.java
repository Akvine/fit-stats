package ru.akvine.fitstats.controllers.rest.dto.product;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ListProductRequest {
    private String filter;
}
