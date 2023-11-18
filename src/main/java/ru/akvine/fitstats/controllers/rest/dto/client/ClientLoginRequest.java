package ru.akvine.fitstats.controllers.rest.dto.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientLoginRequest {
    private String email;
    private String password;
}
