package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UnblockClientRequest extends SecretRequest {
    private String uuid;
    private String email;
}
