package ru.akvine.fitstats.services.dto.statistic;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.services.dto.DateRange;

@Data
@SuperBuilder
public abstract class Statistic {
    private String clientUuid;
    @Nullable
    private Integer roundAccuracy;
    private DateRange dateRange;
}
