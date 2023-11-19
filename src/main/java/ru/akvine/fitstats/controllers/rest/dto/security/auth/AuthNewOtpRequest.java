package ru.akvine.fitstats.controllers.rest.dto.security.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthNewOtpRequest extends LoginRequest {
}
