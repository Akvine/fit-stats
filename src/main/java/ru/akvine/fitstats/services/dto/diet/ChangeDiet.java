package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.Diet;

@Data
@Accessors(chain = true)
public class ChangeDiet {
    private String clientUuid;
    private Diet diet;
}
