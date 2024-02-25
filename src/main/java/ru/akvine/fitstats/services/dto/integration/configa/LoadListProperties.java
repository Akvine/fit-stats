package ru.akvine.fitstats.services.dto.integration.configa;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class LoadListProperties {
    @NotBlank
    private String appUuid;
}
