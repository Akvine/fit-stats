package ru.akvine.fitstats.services.dto.weight;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class ChangeWeight {
    private String clientUuid;
    private String weight;
    private LocalDate date;
}
