package ru.akvine.fitstats.controllers.rest.dto.statistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class DateRangeInfo {
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    private String duration;
}
