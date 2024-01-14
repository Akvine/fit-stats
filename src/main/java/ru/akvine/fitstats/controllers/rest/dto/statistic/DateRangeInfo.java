package ru.akvine.fitstats.controllers.rest.dto.statistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class DateRangeInfo {
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    private String duration;
}
