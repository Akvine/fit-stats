package ru.akvine.fitstats.controllers.rest.dto.common;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.utils.UUIDGenerator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Successful response status
 */
@Data
@Accessors(chain = true)
public class SuccessfulResponse implements Response {

    /**
     * Request unique identificator <br/>
     *
     * <b>Example</b>: gbhjnkme-rdcfgv-hbjnkm-7689ui-okp3ew
     */
    @NotBlank
    private final String requestId = UUIDGenerator.uuid();

    /**
     * Result response status
     */
    @NotNull
    private final ResponseStatus status = ResponseStatus.SUCCESS;
}