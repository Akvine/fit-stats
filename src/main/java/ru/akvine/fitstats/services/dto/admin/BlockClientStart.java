package ru.akvine.fitstats.services.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BlockClientStart {
    private String uuid;
    private String email;
    private long minutes;
}
