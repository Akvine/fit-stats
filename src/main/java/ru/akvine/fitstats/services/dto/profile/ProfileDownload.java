package ru.akvine.fitstats.services.dto.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;

import java.time.LocalDate;

@Getter
@Setter
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
