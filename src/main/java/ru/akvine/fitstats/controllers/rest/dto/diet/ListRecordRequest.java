package ru.akvine.fitstats.controllers.rest.dto.diet;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class ListRecordRequest {
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;

    private LocalTime time;
}
