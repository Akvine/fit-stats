package ru.akvine.fitstats.services.dto.diet;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;


@Getter
@Setter
@Accessors(chain = true)
public class Display {
    private String clientUuid;
    @Nullable
    private LocalDate date;
}
