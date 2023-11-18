package ru.akvine.fitstats.controllers.rest.dto.profile.file;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DietRecordCsvRow {
    @CsvBindByPosition(position = 0)
    private String proteins;

    @CsvBindByPosition(position = 1)
    private String fats;

    @CsvBindByPosition(position = 2)
    private String carbohydrates;

    @CsvBindByPosition(position = 3)
    private String calories;

    @CsvBindByPosition(position = 4)
    private String volume;

    @CsvBindByPosition(position = 5)
    private String product;

    @CsvBindByPosition(position = 6)
    private String uuid;

    @CsvBindByPosition(position = 7)
    private String measurement;

    @CsvBindByPosition(position = 8)
    private String date;

    @CsvBindByPosition(position = 9)
    private String time;
}
