package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.services.dto.DateRange;

@Data
@Accessors(chain = true)
public class DeleteRecord {
    private String clientUuid;
    @Nullable
    private String recordUuid;
    @Nullable
    private DateRange dateRange;
}
