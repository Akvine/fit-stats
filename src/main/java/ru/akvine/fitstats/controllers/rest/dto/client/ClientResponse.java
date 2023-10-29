package ru.akvine.fitstats.controllers.rest.dto.client;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
public class ClientResponse extends SuccessfulResponse {
    @NotNull
    @Valid
    private ClientDto client;
}
