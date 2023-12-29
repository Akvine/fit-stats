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
import ru.akvine.fitstats.controllers.rest.meta.DietControllerMeta;
import ru.akvine.fitstats.services.DietService;
import ru.akvine.fitstats.services.dto.diet.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DietController implements DietControllerMeta {
    private final DietConverter dietConverter;
    private final DietService dietService;

    @Override
    public Response add(@Valid AddRecordRequest request) {
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
}
