package ru.akvine.fitstats.controllers.rest.dto.security.access_restore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccessRestoreStartRequest extends LoginRequest {
}
