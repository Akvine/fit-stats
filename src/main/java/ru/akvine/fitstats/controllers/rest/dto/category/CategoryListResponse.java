package ru.akvine.fitstats.controllers.rest.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class CategoryListResponse extends SuccessfulResponse {
    @Valid
    @NotNull
    private List<CategoryDto> categories;
}
