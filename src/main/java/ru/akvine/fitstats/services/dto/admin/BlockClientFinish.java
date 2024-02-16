package ru.akvine.fitstats.services.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BlockClientFinish {
    private String email;
    private LocalDateTime dateTime;
    private long minutes;
}
