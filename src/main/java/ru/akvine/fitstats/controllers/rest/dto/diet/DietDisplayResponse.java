package ru.akvine.fitstats.controllers.rest.dto.diet;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
public class DietDisplayResponse extends SuccessfulResponse {
    @Valid
    @NotNull
    private DietDisplayInfoDto dietDisplayInfo;
}
