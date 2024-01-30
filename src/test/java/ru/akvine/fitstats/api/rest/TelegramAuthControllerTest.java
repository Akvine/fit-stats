package ru.akvine.fitstats.api.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.akvine.fitstats.api.config.RestMethods;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.akvine.fitstats.api.rest.TestConstants.*;

@DisplayName("Telegram auth controller")
public class TelegramAuthControllerTest extends ApiBaseTest {

    @DisplayName("FAIL - no session")
    @Test
    public void testAuthCodeGenerate_fail_noSession() throws Exception {
        doPost(RestMethods.TELEGRAM_AUTH_CODE_GENERATE)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @DisplayName("SUCCESS - generate code")
    @Test
    public void testAuthCodeGenerate_success_case1() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_26).getValue();

        doPost(RestMethods.TELEGRAM_AUTH_CODE_GENERATE, sessionId, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.code").exists());
    }

    @DisplayName("FAIL - no session")
    @Test
    public void testAuthCodeGet_fail_noSession() throws Exception {
        doPost(RestMethods.TELEGRAM_AUTH_CODE_GET)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @DisplayName("FAIL - telegram auth code not exists")
    @Test
    public void testAuthCodeGet_fail_telegramAuthCodeNotExists() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_27).getValue();

        doPost(RestMethods.TELEGRAM_AUTH_CODE_GET, sessionId, null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Telegram.TELEGRAM_AUTH_CODE_NOT_FOUND_ERROR));
    }

    @DisplayName("FAIL - telegram auth code is expired")
    @Test
    public void testAuthCodeGet_fail_telegramAuthCodeExpired() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_28).getValue();

        doPost(RestMethods.TELEGRAM_AUTH_CODE_GET, sessionId, null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Telegram.TELEGRAM_AUTH_CODE_NOT_FOUND_ERROR));
    }

    @DisplayName("SUCCESS - get telegram auth code")
    @Test
    public void testAuthCodeGet_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_29).getValue();

        doPost(RestMethods.TELEGRAM_AUTH_CODE_GET, sessionId, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.code").value("generated_code_2"))
                .andExpect(jsonPath("$.createdDate").value("2024-01-31T00:00"))
                .andExpect(jsonPath("$.expiredAt").value("2999-01-01T00:00"));
    }
}
