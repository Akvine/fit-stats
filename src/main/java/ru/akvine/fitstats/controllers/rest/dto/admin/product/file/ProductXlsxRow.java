package ru.akvine.fitstats.controllers.rest.dto.admin.product.file;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductXlsxRow {
    private String rowNumber;

    private String uuid;

    private String title;

    private String producer;

    private String proteins;

    private String fats;

    private String carbohydrates;

    private String calories;

    private String alcohol;

    private String vol;

    private String volume;

    private String measurement;
}
