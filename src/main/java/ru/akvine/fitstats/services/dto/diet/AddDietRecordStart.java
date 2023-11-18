package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class AddDietRecordStart {
    private String productUuid;
    private String clientUuid;
    private double volume;
    private LocalDate date;
    @Nullable
    private LocalTime time;
}
