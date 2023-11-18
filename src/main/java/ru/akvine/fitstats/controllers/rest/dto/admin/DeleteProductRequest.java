package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class DeleteProductRequest extends SecretRequest {
    @NotBlank
    private String uuid;
}
