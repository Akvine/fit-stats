package ru.akvine.fitstats.services.dto.statistic;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.akvine.fitstats.services.dto.DateRange;

@Data
@SuperBuilder
public abstract class Statistic {
    private String clientUuid;
    private DateRange dateRange;
    private int roundAccuracy;
}
