package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DeleteRecords {
    private String clientUuid;
    private List<String> recordsUuids;
}
