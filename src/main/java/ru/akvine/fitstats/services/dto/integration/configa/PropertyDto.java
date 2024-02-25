package ru.akvine.fitstats.services.dto.integration.configa;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PropertyDto {
    private String name;
    private String value;
    private boolean modifiable;
}
