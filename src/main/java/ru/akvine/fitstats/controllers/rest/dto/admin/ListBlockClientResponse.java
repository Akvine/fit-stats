package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ListBlockClientResponse extends SuccessfulResponse {
    private long count;
    private List<BlockClientDto> list = new ArrayList<>();
}
