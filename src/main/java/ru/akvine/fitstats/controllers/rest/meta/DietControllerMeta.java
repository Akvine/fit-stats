package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.diet.AddRecordRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.DeleteRecordRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.DisplayRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.ListRecordRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.ChangeDietRequest;

import javax.validation.Valid;

@RequestMapping(value = "/diet")
public interface DietControllerMeta {
    @PostMapping(value = "/records/add")
    Response add(@Valid @RequestBody AddRecordRequest request);

    @GetMapping(value = "/records/list")
    Response list(@Valid @RequestBody ListRecordRequest request);

    @PostMapping(value = "/records/delete")
    Response delete(@Valid @RequestBody DeleteRecordRequest request);

    @GetMapping(value = "/display")
    Response display(@Valid @RequestBody DisplayRequest request);

    @PostMapping(value = "/change")
    Response change(@Valid @RequestBody ChangeDietRequest request);
}
