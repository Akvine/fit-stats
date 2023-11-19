package ru.akvine.fitstats.controllers.rest.dto.security.registration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegistrationStartRequest extends LoginRequest {
}
