package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.weight.ChangeWeightRequest;
import ru.akvine.fitstats.controllers.rest.dto.weight.DeleteWeightRequest;

import javax.validation.Valid;

@RequestMapping(value = "/weight")
public interface WeightControllerMeta {
    @PostMapping(value = "/record/list")
    Response list();

    @PostMapping(value = "/record/change")
    Response change(@Valid @RequestBody ChangeWeightRequest request);

    @PostMapping(value = "/record/delete")
    Response delete(@Valid @RequestBody DeleteWeightRequest request);
}
