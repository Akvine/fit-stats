package ru.akvine.fitstats.controllers.rest.dto.admin.product;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ImportProducts {
    private String clientUuid;
    private List<?> records;
}
