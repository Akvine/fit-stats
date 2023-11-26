package ru.akvine.fitstats.controllers.rest.dto.weight;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import java.util.Map;

@Data
@Accessors(chain = true)
public class ListWeightResponse extends SuccessfulResponse {
    private Map<String, String> info;
}
