package ru.akvine.fitstats.controllers.rest.dto.profile.file;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DietRecordXlsxRow {
    private String rowNumber;

    private String proteins;

    private String fats;

    private String carbohydrates;

    private String calories;

    private String vol;

    private String alcohol;

    private String volume;

    private String product;

    private String uuid;

    private String measurement;

    private String date;

    private String time;
}
