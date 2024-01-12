package ru.akvine.api.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.akvine.api.config.RestMethods;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationStartRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Registration start")
public class RegistrationControllerTest extends ApiBaseTest {
    private final static String CLIENT_EMAIL_1 = "testEmail1@gmail.com";

    @Test
    @DisplayName("FAIL - invalid email")
    public void testRegistrationStart_fail_invalidEmail() throws Exception {
        RegistrationStartRequest request = (RegistrationStartRequest) new RegistrationStartRequest()
                .setLogin("email");

        doPost(RestMethods.REGISTRATION_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }

    @Test
    public void testRegistrationStart_success() throws Exception {
        RegistrationStartRequest request = (RegistrationStartRequest) new RegistrationStartRequest()
                .setLogin(CLIENT_EMAIL_1);

        doPost(RestMethods.REGISTRATION_START, request)
                .andExpect(status().isOk());
    }
}
