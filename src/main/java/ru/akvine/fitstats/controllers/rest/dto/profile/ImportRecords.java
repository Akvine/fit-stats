package ru.akvine.fitstats.controllers.rest.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ImportRecords {
    private String clientUuid;
    private List<?> records;
}
