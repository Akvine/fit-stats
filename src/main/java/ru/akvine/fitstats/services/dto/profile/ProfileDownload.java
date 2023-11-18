package ru.akvine.fitstats.services.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class ProfileDownload {
    private String clientUuid;
    private ConverterType converterType;
    @Nullable
    private LocalDate startDate;
    @Nullable
    private LocalDate endDate;
    @Nullable
    private Duration duration;
}
