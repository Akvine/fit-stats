package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.diet.*;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DietRecordDto;
import ru.akvine.fitstats.services.dto.diet.*;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Component
public class DietConverter {
    public AddDietRecordStart convertToDietRecordBean(AddRecordRequest request) {
        Preconditions.checkNotNull(request, "addRecordRequest is null");
        return new AddDietRecordStart()
                .setProductUuid(request.getProductUuid())
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setVolume(request.getVolume())
                .setDate(request.getDate() == null ? LocalDate.now() : request.getDate())
                .setTime(request.getTime() == null ? LocalTime.now() : request.getTime());
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

    public DeleteRecords convertToDeleteRecords(DeleteRecordsRequest request) {
        Preconditions.checkNotNull(request, "deleteRecordsRequest is null");
        return new DeleteRecords()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setRecordsUuids(request.getRecordsUuids());
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

    private DietRecordDto buildDietRecordDto(DietRecordBean dietRecordBean) {
        return new DietRecordDto()
                .setUuid(dietRecordBean.getUuid())
                .setProductUuid(dietRecordBean.getProductBean().getUuid())
                .setProductTitle(dietRecordBean.getProductBean().getTitle())
                .setProteins(dietRecordBean.getProteins())
                .setFats(dietRecordBean.getFats())
                .setCarbohydrates(dietRecordBean.getCarbohydrates())
                .setCalories(dietRecordBean.getCalories())
                .setVolume(dietRecordBean.getVolume())
                .setMeasurement(dietRecordBean.getProductBean().getMeasurement().toString());
    }

    private DietRecordDto buildDietRecordDto(AddDietRecordFinish dietRecordFinish) {
        Preconditions.checkNotNull(dietRecordFinish, "dietRecordFinish is null");
        return new DietRecordDto()
                .setUuid(dietRecordFinish.getUuid())
                .setProductUuid(dietRecordFinish.getProductUuid())
                .setProductTitle(dietRecordFinish.getProductTitle())
                .setProteins(dietRecordFinish.getProteins())
                .setFats(dietRecordFinish.getFats())
                .setCarbohydrates(dietRecordFinish.getCarbohydrates())
                .setCalories(dietRecordFinish.getCalories())
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
