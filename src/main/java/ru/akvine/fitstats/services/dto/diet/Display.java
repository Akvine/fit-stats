package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;


@Data
@Accessors(chain = true)
public class Display {
    private String clientUuid;
    @Nullable
    private LocalDate date;
}
