package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.diet.AddRecordRequest;

import javax.validation.Valid;

@RequestMapping(value = "/diet")
public interface DietControllerMeta {
    @PostMapping(value = "/record/add")
    Response add(@Valid @RequestBody AddRecordRequest request);

    @GetMapping(value = "/display")
    Response display();
}
