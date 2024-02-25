package ru.akvine.fitstats.services.dto.integration.configa;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class LoadListPropertiesResponse extends SuccessfulResponse {
    private String appUuid;
    private List<PropertyDto> properties;
}
