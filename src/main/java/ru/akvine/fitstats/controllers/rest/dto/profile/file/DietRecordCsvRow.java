package ru.akvine.fitstats.controllers.rest.dto.profile.file;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DietRecordCsvRow {
    @CsvBindByPosition(position = 0)
    private String rowNumber;

    @CsvBindByPosition(position = 1)
    private String proteins;

    @CsvBindByPosition(position = 2)
    private String fats;

    @CsvBindByPosition(position = 3)
    private String carbohydrates;

    @CsvBindByPosition(position = 4)
    private String calories;

    @CsvBindByPosition(position = 5)
    private String vol;

    @CsvBindByPosition(position = 6)
    private String alcohol;

    @CsvBindByPosition(position = 7)
    private String volume;

    @CsvBindByPosition(position = 8)
    private String product;

    @CsvBindByPosition(position = 9)
    private String uuid;

    @CsvBindByPosition(position = 10)
    private String measurement;

    @CsvBindByPosition(position = 11)
    private String date;

    @CsvBindByPosition(position = 12)
    private String time;
}
