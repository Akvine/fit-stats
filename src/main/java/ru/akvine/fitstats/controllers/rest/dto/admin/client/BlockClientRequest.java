package ru.akvine.fitstats.controllers.rest.dto.admin.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.admin.SecretRequest;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BlockClientRequest extends SecretRequest {
    private String uuid;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
}
