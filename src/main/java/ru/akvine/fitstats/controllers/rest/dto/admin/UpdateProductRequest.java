package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.UuidRequest;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateProductRequest extends UuidRequest {

    private String title;

    private String producer;

    private Double proteins;

    private Double fats;

    private Double carbohydrates;

    private Double volume;

    private String measurement;
}
