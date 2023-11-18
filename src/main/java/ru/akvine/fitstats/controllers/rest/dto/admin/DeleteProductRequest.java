package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
public class DeleteProductRequest extends SecretRequest {
    @NotBlank
    private String uuid;
}
