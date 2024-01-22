package ru.akvine.fitstats.controllers.rest.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DateRangeRequest;

@Data
@Accessors(chain = true)
public class DeleteRecordRequest extends DateRangeRequest {
    private String uuid;
}
