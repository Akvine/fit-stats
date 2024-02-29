package ru.akvine.fitstats.controllers.rest.dto.admin.client;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.admin.SecretRequest;

@Data
@Accessors(chain = true)
public class UnblockClientRequest extends SecretRequest {
    private String uuid;
    private String email;
}
