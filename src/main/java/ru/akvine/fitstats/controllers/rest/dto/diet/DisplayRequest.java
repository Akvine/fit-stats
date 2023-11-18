package ru.akvine.fitstats.controllers.rest.dto.diet;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class DisplayRequest {
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;
}
