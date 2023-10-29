package ru.akvine.fitstats.controllers.rest.dto.diet;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class AddRecordRequest {
    @NotBlank
    private String productUuid;

    private double volume;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;

    private LocalTime time;
}
