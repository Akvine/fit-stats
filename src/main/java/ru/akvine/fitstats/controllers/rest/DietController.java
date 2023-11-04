package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.DietConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.diet.AddRecordRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.DisplayRequest;
import ru.akvine.fitstats.controllers.rest.meta.DietControllerMeta;
import ru.akvine.fitstats.services.DietService;
import ru.akvine.fitstats.services.dto.diet.AddDietRecordFinish;
import ru.akvine.fitstats.services.dto.diet.AddDietRecordStart;
import ru.akvine.fitstats.services.dto.diet.DietDisplay;
import ru.akvine.fitstats.services.dto.diet.Display;

import javax.validation.Valid;

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
    public Response display(@Valid @RequestBody DisplayRequest request) {
        Display display = dietConverter.convertToDisplay(request);
        DietDisplay dietDisplay = dietService.display(display);
        return dietConverter.convertToDietDisplayResponse(dietDisplay);
    }
}
