package ru.akvine.fitstats.controllers.rest.dto.admin.product;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.admin.SecretRequest;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class DeleteProductRequest extends SecretRequest {
    @NotBlank
    private String uuid;
}
