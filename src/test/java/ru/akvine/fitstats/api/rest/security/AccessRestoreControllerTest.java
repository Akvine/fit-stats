package ru.akvine.fitstats.api.rest.security;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.akvine.fitstats.api.config.RestMethods;
import ru.akvine.fitstats.api.rest.ApiBaseTest;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreCheckOtpRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreStartRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.akvine.fitstats.api.rest.TestConstants.*;
import static ru.akvine.fitstats.api.rest.TestConstants.OTP_INVALID_ATTEMPTS_LEFT;

@DisplayName("Access controller")
public class AccessRestoreControllerTest extends ApiBaseTest {

    @Test
    @DisplayName("FAIL - email is blank")
    public void testAccessRestoreStart_fail_emailIsBlank() throws Exception {
        AccessRestoreStartRequest request = new AccessRestoreStartRequest();

        doPost(RestMethods.ACCESS_RESTORE_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - email is invalid")
    public void testAccessRestoreStart_fail_emailIsInvalid() throws Exception {
        AccessRestoreStartRequest request = (AccessRestoreStartRequest) new AccessRestoreStartRequest()
                .setLogin("invalid email");

        doPost(RestMethods.ACCESS_RESTORE_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - client with email not registered")
    public void testAccessRestoreStart_fail_clientWithEmailNotRegistered() throws Exception {
        AccessRestoreStartRequest request = (AccessRestoreStartRequest) new AccessRestoreStartRequest()
                .setLogin(CLIENT_EMAIL_NOT_EXISTS_4);

        doPost(RestMethods.ACCESS_RESTORE_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Client.CLIENT_NOT_FOUND_ERROR));
    }

    @Test
    @DisplayName("SUCCESS - access restore start")
    public void testAccessRestoreStart_success() throws Exception {
        AccessRestoreStartRequest request = (AccessRestoreStartRequest) new AccessRestoreStartRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_21);

        doPost(RestMethods.ACCESS_RESTORE_START, request)
                .andExpect(isAccessRestoreStartedSuccessful());
    }

    @Test
    @DisplayName("FAIL - email is blank")
    public void testAccessRestoreNewOtp_fail_emailIsBlank() throws Exception {
        AccessRestoreStartRequest request = new AccessRestoreStartRequest();

        doPost(RestMethods.ACCESS_RESTORE_NEWOTP, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - email is invalid")
    public void testAccessRestoreNewOtp_fail_emailIsInvalid() throws Exception {
        AccessRestoreStartRequest request = (AccessRestoreStartRequest) new AccessRestoreStartRequest()
                .setLogin("invalid email");

        doPost(RestMethods.ACCESS_RESTORE_NEWOTP, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - client with email not registered")
    public void testAccessRestoreNewOtp_fail_clientWithEmailNotRegistered() throws Exception {
        AccessRestoreStartRequest request = (AccessRestoreStartRequest) new AccessRestoreStartRequest()
                .setLogin(CLIENT_EMAIL_NOT_EXISTS_4);

        doPost(RestMethods.ACCESS_RESTORE_NEWOTP, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Client.CLIENT_NOT_FOUND_ERROR));
    }

    @Test
    @DisplayName("FAIL - action not started")
    public void testAccessRestoreNewOtp_fail_actionNotStarted() throws Exception {
        AccessRestoreStartRequest request = (AccessRestoreStartRequest) new AccessRestoreStartRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_22);

        doPost(RestMethods.ACCESS_RESTORE_NEWOTP, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Security.ACTION_NOT_STARTED_ERROR));
    }

    @Test
    @DisplayName("SUCCESS - new otp send success")
    public void testAccessRestoreNewOtp_success() throws Exception {
        AccessRestoreStartRequest request = (AccessRestoreStartRequest) new AccessRestoreStartRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_23);

        doPost(RestMethods.ACCESS_RESTORE_START, request)
                .andExpect(isAccessRestoreStartedSuccessful());

        doPost(RestMethods.ACCESS_RESTORE_NEWOTP, request)
                .andExpect(isAccessRestoreStartedSuccessful());
    }

    @Test
    @DisplayName("FAIL - email is null")
    public void testAccessRestoreCheck_fail_emailIsNull() throws Exception {
        AccessRestoreCheckOtpRequest request = new AccessRestoreCheckOtpRequest()
                .setOtp(OTP);

        doPost(RestMethods.ACCESS_RESTORE_CHECK, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - otp is null")
    public void testAccessRestoreCheck_fail_otpIsNull() throws Exception {
        AccessRestoreCheckOtpRequest request = (AccessRestoreCheckOtpRequest) new AccessRestoreCheckOtpRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_24);

        doPost(RestMethods.ACCESS_RESTORE_CHECK, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - email is invalid")
    public void testAccessRestoreCheck_fail_emailIsInvalid() throws Exception {
        AccessRestoreCheckOtpRequest request = (AccessRestoreCheckOtpRequest) new AccessRestoreCheckOtpRequest()
                .setOtp(OTP)
                .setLogin("invalid email");

        doPost(RestMethods.ACCESS_RESTORE_CHECK, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - client with email not registered")
    public void testAccessRestoreCheck_fail_clientWithEmailNotRegistered() throws Exception {
        AccessRestoreCheckOtpRequest request = (AccessRestoreCheckOtpRequest) new AccessRestoreCheckOtpRequest()
                .setOtp(OTP)
                .setLogin(CLIENT_EMAIL_NOT_EXISTS_5);

        doPost(RestMethods.ACCESS_RESTORE_CHECK, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Client.CLIENT_NOT_FOUND_ERROR));
    }

    @Test
    @DisplayName("FAIL - no session")
    public void testAccessRestoreCheck_fail_noSession() throws Exception {
        AccessRestoreCheckOtpRequest request = (AccessRestoreCheckOtpRequest) new AccessRestoreCheckOtpRequest()
                .setOtp(OTP)
                .setLogin(CLIENT_EMAIL_EXISTS_24);

        doPost(RestMethods.ACCESS_RESTORE_CHECK, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @Test
    @DisplayName("SUCCESS - check otp")
    public void testAccessRestoreCheck_success() throws Exception {
        AccessRestoreStartRequest request = (AccessRestoreStartRequest) new AccessRestoreStartRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_24);

        Cookie session = doPost(RestMethods.ACCESS_RESTORE_START, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andReturn().getResponse().getCookie(SESSION_COOKIE_NAME);
        String sessionId = session.getValue();

        AccessRestoreCheckOtpRequest accessRestoreCheckOtpRequest = (AccessRestoreCheckOtpRequest) new AccessRestoreCheckOtpRequest()
                .setOtp(OTP)
                .setLogin(CLIENT_EMAIL_EXISTS_24);

        doPost(RestMethods.ACCESS_RESTORE_CHECK, sessionId, accessRestoreCheckOtpRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));
    }

    @Test
    @DisplayName("FAIL - email is null")
    public void testAccessRestoreFinish_fail_emailIsNull() throws Exception {
        AccessRestoreFinishRequest request = new AccessRestoreFinishRequest()
                .setPassword(VALID_PASSWORD);

        doPost(RestMethods.ACCESS_RESTORE_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - password is null")
    public void testAccessRestoreFinish_fail_passwordIsNull() throws Exception {
        AccessRestoreFinishRequest request = (AccessRestoreFinishRequest) new AccessRestoreFinishRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_25);

        doPost(RestMethods.ACCESS_RESTORE_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - password is invalid")
    public void testAccessRestoreFinish_fail_passwordIsInvalid() throws Exception {
        AccessRestoreFinishRequest request = (AccessRestoreFinishRequest) new AccessRestoreFinishRequest()
                .setPassword(INVALID_PASSWORD)
                .setLogin(CLIENT_EMAIL_EXISTS_25);

        doPost(RestMethods.ACCESS_RESTORE_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Security.REGISTRATION_PASSWORD_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - no session")
    public void testAccessRestoreFinish_fail_noSession() throws Exception {
        AccessRestoreFinishRequest request = (AccessRestoreFinishRequest) new AccessRestoreFinishRequest()
                .setPassword(VALID_PASSWORD)
                .setLogin(CLIENT_EMAIL_EXISTS_25);

        doPost(RestMethods.ACCESS_RESTORE_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @Test
    @DisplayName("SUCCESS - finish")
    public void testAccessRestoreFinish_success() throws Exception {
        AccessRestoreStartRequest accessRestoreStartRequest = (AccessRestoreStartRequest) new AccessRestoreStartRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_25);

        Cookie session = doPost(RestMethods.ACCESS_RESTORE_START, accessRestoreStartRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andReturn().getResponse().getCookie(SESSION_COOKIE_NAME);
        String sessionId = session.getValue();

        AccessRestoreCheckOtpRequest accessRestoreCheckOtpRequest = (AccessRestoreCheckOtpRequest) new AccessRestoreCheckOtpRequest()
                .setOtp(OTP)
                .setLogin(CLIENT_EMAIL_EXISTS_25);

        doPost(RestMethods.ACCESS_RESTORE_CHECK, sessionId, accessRestoreCheckOtpRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));

        AccessRestoreFinishRequest accessRestoreFinishRequest = (AccessRestoreFinishRequest) new AccessRestoreFinishRequest()
                .setPassword(VALID_PASSWORD)
                .setLogin(CLIENT_EMAIL_EXISTS_25);

        doPost(RestMethods.ACCESS_RESTORE_FINISH, sessionId, accessRestoreFinishRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.requestId").exists());
    }

    private static ResultMatcher isAccessRestoreStartedSuccessful() {
        return response -> {
            status().isOk().match(response);
            jsonPath("$.status").value(REQUEST_STATUS_SUCCESS).match(response);
            jsonPath("$.state").value(OTP_NEW_STATE);
            jsonPath("$.otp.otpNumber").value(OTP_NUMBER).match(response);
            jsonPath("$.otp.otpCountLeft").value(OTP_COUNT_LEFT).match(response);
            jsonPath("$.otp.otpLastUpdate").exists();
            jsonPath("$.otp.newOtpDelay").value(OTP_NEW_DELAY);
            jsonPath("$.otp.otpInvalidAttemptsLeft").value(OTP_INVALID_ATTEMPTS_LEFT);
        };
    }
}
