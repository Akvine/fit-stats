package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.weight.ChangeWeightRequest;
import ru.akvine.fitstats.controllers.rest.dto.weight.ListWeightResponse;
import ru.akvine.fitstats.services.dto.weight.ChangeWeight;
import ru.akvine.fitstats.services.dto.weight.ListWeightResult;
import ru.akvine.fitstats.utils.SecurityUtils;

@Component
public class WeightConverter {
    public ChangeWeight convertToChangeWeight(ChangeWeightRequest request) {
        Preconditions.checkNotNull(request, "changeWeightRequest is null");
        return new ChangeWeight()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setWeight(request.getWeight())
                .setDate(request.getDate());
    }

    public ListWeightResponse convertToListWeightResponse(ListWeightResult listWeightResult) {
        Preconditions.checkNotNull(listWeightResult, "listWeightResult is null");
        return new ListWeightResponse()
                .setInfo(listWeightResult.getWeight());
    }
}
