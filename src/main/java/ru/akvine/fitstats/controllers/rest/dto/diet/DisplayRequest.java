package ru.akvine.fitstats.controllers.rest.dto.diet;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class DisplayRequest {
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;
}
