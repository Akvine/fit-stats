package ru.akvine.fitstats.controllers.rest.dto.telegram;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class GenerateAuthCodeResponse extends SuccessfulResponse {
    @NotBlank
    private String code;

    @NotBlank
    private String createdDate;

    @NotBlank
    private String expiredAt;
}
