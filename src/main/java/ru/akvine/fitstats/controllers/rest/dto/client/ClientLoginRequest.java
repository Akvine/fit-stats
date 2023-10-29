package ru.akvine.fitstats.controllers.rest.dto.client;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ClientLoginRequest {
    private String email;
    private String password;
}
