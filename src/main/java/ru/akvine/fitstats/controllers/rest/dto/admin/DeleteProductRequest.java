package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.UuidRequest;

@Getter
@Setter
@Accessors(chain = true)
public class DeleteProductRequest extends UuidRequest {
}
