package ru.akvine.fitstats.controllers.rest.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BlockClientRequest extends SecretRequest {
    private String uuid;
    private String email;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime date;
}
