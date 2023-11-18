package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class ListRecord {
    private String clientUuid;
    private LocalDate date;
    @Nullable
    private LocalTime time;
}
