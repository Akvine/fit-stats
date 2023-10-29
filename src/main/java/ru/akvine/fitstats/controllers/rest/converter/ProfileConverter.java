package ru.akvine.fitstats.controllers.rest.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.time.LocalDate;

@Component
public class ProfileConverter {
    public ProfileDownload convertToProfileDownload(
            LocalDate startDate,
            LocalDate endDate,
            String duration,
            String converterType) {
        return new ProfileDownload()
                .setConverterType(converterType == null ? ConverterType.CSV : ConverterType.valueOf(converterType))
                .setDuration(StringUtils.isBlank(duration) ? null : Duration.valueOf(duration))
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid());
    }
}
