package ru.akvine.fitstats.services.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UnblockClient {
    private String uuid;
    private String email;
}
