package ru.akvine.fitstats.controllers.rest.dto.security.access_restore;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class AccessRestoreFinishRequest extends LoginRequest {
    @NotBlank
    private String password;
}
