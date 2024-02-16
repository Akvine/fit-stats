package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BlockClientResponse extends SuccessfulResponse {
    private String email;
    private LocalDateTime dateTime;
    private long minutes;
}
