package ru.akvine.fitstats.controllers.rest.dto.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientDto {
    private String uuid;
    private String email;
    private String firstName;
    private String secondName;
    private String thirdName;
}
