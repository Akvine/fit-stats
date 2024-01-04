package ru.akvine.api.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import ru.akvine.api.config.RestMethods;
import ru.akvine.api.config.TestConfiguration;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationStartRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Registration start")
@Import(TestConfiguration.class)
public class RegistrationControllerTest extends ApiBaseTest {

    @Test
    @DisplayName("FAIL - invalid email")
    public void testRegistrationStart_fail_invalidEmail() throws Exception {
        RegistrationStartRequest request = (RegistrationStartRequest) new RegistrationStartRequest()
                .setLogin("email");

        doPost(RestMethods.REGISTRATION_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }
}
