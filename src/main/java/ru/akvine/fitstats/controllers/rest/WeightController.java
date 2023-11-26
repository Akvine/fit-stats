package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.WeightConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.weight.ChangeWeightRequest;
import ru.akvine.fitstats.controllers.rest.dto.weight.DeleteWeightRequest;
import ru.akvine.fitstats.controllers.rest.meta.WeightControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.WeightValidator;
import ru.akvine.fitstats.services.WeightService;
import ru.akvine.fitstats.services.dto.weight.ChangeWeight;
import ru.akvine.fitstats.services.dto.weight.ListWeightResult;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class WeightController implements WeightControllerMeta {
    private final WeightValidator weightValidator;
    private final WeightConverter weightConverter;
    private final WeightService weightService;

    @Override
    public Response list() {
        ListWeightResult result = weightService.list();
        return weightConverter.convertToListWeightResponse(result);
    }

    @Override
    public Response change(@Valid @RequestBody ChangeWeightRequest request) {
        weightValidator.verifyChangeWeightRequest(request);
        ChangeWeight changeWeight = weightConverter.convertToChangeWeight(request);
        weightService.change(changeWeight);
        return new SuccessfulResponse();
    }

    @Override
    public Response delete(@Valid DeleteWeightRequest request) {
        weightService.delete(request.getDate());
        return new SuccessfulResponse();
    }
}
