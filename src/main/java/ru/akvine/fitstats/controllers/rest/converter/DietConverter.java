package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.diet.*;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DateRangeInfo;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DietRecordDto;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.diet.*;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Component
@RequiredArgsConstructor
public class DietConverter {
    private final PropertyParseService propertyParseService;

    @Value("round.accuracy")
    private String roundAccuracy;

    public AddDietRecordStart convertToDietRecordBean(AddRecordRequest request) {
        Preconditions.checkNotNull(request, "addRecordRequest is null");
        return new AddDietRecordStart()
                .setProductUuid(request.getProductUuid())
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setVolume(request.getVolume())
                .setDate(request.getDate() == null ? LocalDate.now() : request.getDate())
                .setTime(request.getTime());
    }

    public DietRecordResponse convertDietRecordResponse(AddDietRecordFinish dietRecordFinish) {
        Preconditions.checkNotNull(dietRecordFinish, "dietRecordFinish is null");
        return new DietRecordResponse()
                .setDietRecord(buildDietRecordDto(dietRecordFinish));

    }

    public ListRecord convertToListRecord(ListRecordRequest request) {
        Preconditions.checkNotNull(request, "listRecordRequest is null");
        return new ListRecord()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setDate(request.getDate())
                .setTime(request.getTime());
    }

    public DeleteRecord convertToDeleteRecord(DeleteRecordRequest request) {
        Preconditions.checkNotNull(request, "deleteRecordRequest is null");

        DateRangeInfo dateRangeInfo = request.getDateRangeInfo();
        if (dateRangeInfo != null) {
            return new DeleteRecord()
                    .setDateRange(new DateRange()
                            .setDuration(StringUtils.isBlank(dateRangeInfo.getDuration()) ? null : Duration.valueOf(dateRangeInfo.getDuration()))
                            .setStartDate(request.getDateRangeInfo().getStartDate())
                            .setEndDate(request.getDateRangeInfo().getEndDate()))
                    .setClientUuid(SecurityUtils.getCurrentUser().getUuid());
        }
        return new DeleteRecord()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setRecordUuid(request.getUuid());
    }

    public DietRecordListResponse convertToDietRecordListResponse(List<DietRecordBean> records) {
        Preconditions.checkNotNull(records, "records is null");
        return new DietRecordListResponse()
                .setRecords(records
                        .stream()
                        .map(this::buildDietRecordDto)
                        .collect(Collectors.toList()));
    }

    public DietDisplayResponse convertToDietDisplayResponse(DietDisplay dietDisplay) {
        Preconditions.checkNotNull(dietDisplay, "dietDisplay is null");
        return new DietDisplayResponse()
                .setDietDisplayInfo(buildDietDisplayInfoDto(dietDisplay));

    }

    public Display convertToDisplay(DisplayRequest request) {
        Preconditions.checkNotNull(request, "displayRequest is null");
        return new Display()
                .setDate(request.getDate())
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid());
    }

    public ChangeDiet convertToChangeDiet(ChangeDietRequest request) {
        Preconditions.checkNotNull(request, "ChangeDietRequest is null");
        return new ChangeDiet()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setDiet(Diet.valueOf(request.getDietType()));
    }

    private DietRecordDto buildDietRecordDto(DietRecordBean dietRecordBean) {
        int accuracy = propertyParseService.parseInteger(roundAccuracy);
        return new DietRecordDto()
                .setUuid(dietRecordBean.getUuid())
                .setProductUuid(dietRecordBean.getProductBean().getUuid())
                .setProductTitle(dietRecordBean.getProductBean().getTitle())
                .setProteins(dietRecordBean.getProteins())
                .setFats(round(dietRecordBean.getFats(), accuracy))
                .setCarbohydrates(round(dietRecordBean.getCarbohydrates(), accuracy))
                .setAlcohol(round(dietRecordBean.getAlcohol(), accuracy))
                .setCalories(round(dietRecordBean.getCalories(), accuracy))
                .setVolume(dietRecordBean.getVolume())
                .setMeasurement(dietRecordBean.getProductBean().getMeasurement().toString());
    }

    private DietRecordDto buildDietRecordDto(AddDietRecordFinish dietRecordFinish) {
        Preconditions.checkNotNull(dietRecordFinish, "dietRecordFinish is null");
        int accuracy = propertyParseService.parseInteger(roundAccuracy);
        return new DietRecordDto()
                .setUuid(dietRecordFinish.getUuid())
                .setProductUuid(dietRecordFinish.getProductUuid())
                .setProductTitle(dietRecordFinish.getProductTitle())
                .setProteins(round(dietRecordFinish.getProteins(), accuracy))
                .setFats(round(dietRecordFinish.getFats(), accuracy))
                .setCarbohydrates(round(dietRecordFinish.getCarbohydrates(), accuracy))
                .setAlcohol(round(dietRecordFinish.getAlcohol(), accuracy))
                .setCalories(round(dietRecordFinish.getCalories(), accuracy))
                .setVolume(dietRecordFinish.getVolume())
                .setMeasurement(dietRecordFinish.getVolumeMeasurement().toString());
    }

    private DietDisplayInfoDto buildDietDisplayInfoDto(DietDisplay dietDisplay) {
        return new DietDisplayInfoDto()
                .setCurrentCalories(round(dietDisplay.getCurrentCalories()))
                .setCurrentCarbohydrates(round(dietDisplay.getCurrentCarbohydrates()))
                .setCurrentFats(round(dietDisplay.getCurrentFats()))
                .setCurrentProteins(round(dietDisplay.getCurrentProteins()))
                .setMaxCalories(round(dietDisplay.getMaxCalories()))
                .setMaxCarbohydrates(round(dietDisplay.getMaxCarbohydrates()))
                .setMaxFats(round(dietDisplay.getMaxFats()))
                .setMaxProteins(round(dietDisplay.getMaxProteins()))
                .setRemainingProteins(round(dietDisplay.getRemainingProteins()))
                .setRemainingFats(round(dietDisplay.getRemainingFats()))
                .setRemainingCarbohydrates(round(dietDisplay.getRemainingCarbohydrates()))
                .setRemainingCalories(round(dietDisplay.getRemainingCalories()));
    }
}
