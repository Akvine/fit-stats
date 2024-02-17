package ru.akvine.fitstats.services.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BlockClientEntry {
    private String email;
    private long minutes;
    private LocalDateTime blockStartDate;
    private LocalDateTime blockEndDate;
}
