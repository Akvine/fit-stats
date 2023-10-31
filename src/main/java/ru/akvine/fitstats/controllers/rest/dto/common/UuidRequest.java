package ru.akvine.fitstats.controllers.rest.dto.common;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Accessors(chain = true)
public class UuidRequest {
    @NotBlank
    private String uuid;
}
