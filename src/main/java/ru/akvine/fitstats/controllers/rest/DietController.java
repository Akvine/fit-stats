package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.DietConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.diet.AddRecordRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.DeleteRecordRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.DisplayRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.ListRecordRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.ChangeDietRequest;
import ru.akvine.fitstats.controllers.rest.meta.DietControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.DietValidator;
import ru.akvine.fitstats.services.DietService;
import ru.akvine.fitstats.services.dto.diet.*;
import ru.akvine.fitstats.services.dto.diet.ChangeDiet;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DietController implements DietControllerMeta {
    private final DietConverter dietConverter;
    private final DietService dietService;
    private final DietValidator dietValidator;

    @Override
    public Response add(@Valid AddRecordRequest request) {
        dietValidator.verifyAddDietRecordRequest(request);
        AddDietRecordStart addDietRecordStart = dietConverter.convertToDietRecordBean(request);
        AddDietRecordFinish addDietRecordFinish = dietService.add(addDietRecordStart);
        return dietConverter.convertDietRecordResponse(addDietRecordFinish);
    }

    @Override
    public Response list(@Valid ListRecordRequest request) {
        ListRecord listRecord = dietConverter.convertToListRecord(request);
        List<DietRecordBean> records = dietService.list(listRecord);
        return dietConverter.convertToDietRecordListResponse(records);
    }

    @Override
    public Response delete(@Valid DeleteRecordRequest request) {
        dietValidator.verifyDeleteDietRequest(request);
        DeleteRecord deleteRecord = dietConverter.convertToDeleteRecord(request);
        dietService.deleteRecord(deleteRecord);
        return new SuccessfulResponse();
    }

    @Override
    public Response display(@Valid DisplayRequest request) {
        Display display = dietConverter.convertToDisplay(request);
        DietDisplay dietDisplay = dietService.display(display);
        return dietConverter.convertToDietDisplayResponse(dietDisplay);
    }

    @Override
    public Response change(@Valid ChangeDietRequest request) {
        dietValidator.verifyUpdateDietRequest(request);
        ChangeDiet changeDiet = dietConverter.convertToChangeDiet(request);
        dietService.changeDiet(changeDiet);
        return new SuccessfulResponse();
    }
}
