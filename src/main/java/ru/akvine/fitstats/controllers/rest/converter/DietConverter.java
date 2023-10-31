package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.diet.AddRecordRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.DietDisplayInfoDto;
import ru.akvine.fitstats.controllers.rest.dto.diet.DietDisplayResponse;
import ru.akvine.fitstats.controllers.rest.dto.diet.DietRecordResponse;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DietRecordDto;
import ru.akvine.fitstats.services.dto.diet.AddDietRecordFinish;
import ru.akvine.fitstats.services.dto.diet.AddDietRecordStart;
import ru.akvine.fitstats.services.dto.diet.DietDisplay;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.time.LocalDate;
import java.time.LocalTime;

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

    public DietDisplayResponse convertToDietDisplayResponse(DietDisplay dietDisplay) {
        Preconditions.checkNotNull(dietDisplay, "dietDisplay is null");
        return new DietDisplayResponse()
                .setDietDisplayInfo(buildDietDisplayInfoDto(dietDisplay));

    }

    private DietRecordDto buildDietRecordDto(AddDietRecordFinish dietRecordFinish) {
        Preconditions.checkNotNull(dietRecordFinish, "dietRecordFinish is null");
        return new DietRecordDto()
                .setProductUuid(dietRecordFinish.getProductUuid())
                .setProductTitle(dietRecordFinish.getProductTitle())
                .setProteins(dietRecordFinish.getProteins())
                .setFats(dietRecordFinish.getFats())
                .setCarbohydrates(dietRecordFinish.getCarbohydrates())
                .setCalories(dietRecordFinish.getCalories());
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