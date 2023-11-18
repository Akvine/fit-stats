package ru.akvine.fitstats.controllers.rest.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class DeleteRecordsRequest {
    @NotNull
    private List<String> recordsUuids;
}
