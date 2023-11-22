package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeleteRecord {
    private String clientUuid;
    private String recordUuid;
}
