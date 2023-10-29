package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.profile.DietRecordExport;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.services.processors.format.Converter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static ru.akvine.fitstats.utils.DateUtils.*;

@Service
@Slf4j
public class ProfileService {
    private final Map<ConverterType, Converter> availableConverters;
    private final DietService dietService;

    @Autowired
    public ProfileService(List<Converter> converters,
                          DietService dietService) {
        this.dietService = dietService;
        this.availableConverters = converters
                .stream()
                .collect(toMap(Converter::getType, identity()));
    }

    public byte[] exportRecords(ProfileDownload profileDownload) {
        Preconditions.checkNotNull(profileDownload, "profileDownload is null");

        DateRange getDateRange = getDateRange(profileDownload);
        List<DietRecordExport> dietRecordsExport = dietService.findByDateRange(
                profileDownload.getClientUuid(),
                getDateRange.getStartDate(),
                getDateRange.getEndDate());
        return availableConverters
                .get(profileDownload.getConverterType())
                .convert(dietRecordsExport, DietRecordExport.class);
    }

    private DateRange getDateRange(ProfileDownload profileDownload) {
        LocalDate startDate = profileDownload.getStartDate();
        LocalDate endDate = profileDownload.getEndDate();
        Duration duration = profileDownload.getDuration();

        DateRange findDateRange;

        if (profileDownload.getDuration() != null) {
            switch (duration) {
                case DAY:
                    findDateRange = getDayRange();
                    break;
                case WEEK:
                    findDateRange = getWeekRange();
                    break;
                case MONTH:
                    findDateRange = getMonthRange();
                    break;
                default:
                    findDateRange = getYearRange();
            }
        } else {
            findDateRange = new DateRange(startDate, endDate);
        }
        return findDateRange;
    }
}
