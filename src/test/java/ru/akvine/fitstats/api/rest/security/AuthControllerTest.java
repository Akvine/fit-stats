package ru.akvine.fitstats.api.rest.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.akvine.fitstats.api.config.RestMethods;
import ru.akvine.fitstats.api.rest.ApiBaseTest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthCredentialsRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthNewOtpRequest;
import ru.akvine.fitstats.entities.security.BlockedCredentialsEntity;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.repositories.security.BlockedCredentialsRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.akvine.fitstats.api.rest.TestConstants.*;

@DisplayName("Auth controller")
public class AuthControllerTest extends ApiBaseTest {

    @Autowired
    private BlockedCredentialsRepository blockedCredentialsRepository;

    @Test
    @DisplayName("FAIL - email is blank")
    public void testAuthStart_fail_emailIsBlank() throws Exception {
        AuthCredentialsRequest request = new AuthCredentialsRequest();
        request.setPassword(VALID_PASSWORD);

        doPost(RestMethods.AUTH_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - password is blank")
    public void testAuthStart_fail_passwordIsBlank() throws Exception {
        AuthCredentialsRequest request = new AuthCredentialsRequest();
        request.setLogin(CLIENT_EMAIL_EXISTS_7);

        doPost(RestMethods.AUTH_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }


    @Test
    @DisplayName("FAIL - email is invalid")
    public void testAuthStart_fail_emailIsInvalid() throws Exception {
        AuthCredentialsRequest request = new AuthCredentialsRequest();
        request.setLogin(INVALID_EMAIL);
        request.setPassword(VALID_PASSWORD);

        doPost(RestMethods.AUTH_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }

    @Test
    @DisplayName("SUCCESS - authenticate client")
    public void testAuthStart_success() throws Exception {
        AuthCredentialsRequest request = new AuthCredentialsRequest();
        request.setLogin(CLIENT_EMAIL_EXISTS_7);
        request.setPassword(VALID_PASSWORD);

        doPost(RestMethods.AUTH_START, request)
                .andExpect(isAuthStartedSuccessful());
    }

    @Test
    @DisplayName("FAIL - email is blank")
    public void testAuthNewOtp_fail_emailIsBlank() throws Exception {
        AuthNewOtpRequest request = new AuthNewOtpRequest();

        doPost(RestMethods.AUTH_NEW_OTP, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - email is invalid")
    public void testAuthNewOtp_fail_emailIsInvalid() throws Exception {
        AuthNewOtpRequest request = new AuthNewOtpRequest();
        request.setLogin(INVALID_EMAIL);

        doPost(RestMethods.AUTH_NEW_OTP, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }


    @Test
    @DisplayName("FAIL - auth action not started")
    public void testAuthNewOtp_fail_authActionNotStarted() throws Exception {
        AuthNewOtpRequest request = new AuthNewOtpRequest();
        request.setLogin(CLIENT_EMAIL_EXISTS_8);

        doPost(RestMethods.AUTH_NEW_OTP, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Security.ACTION_NOT_STARTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - client is blocked")
    public void testAuthNewOtp_fail_clientIsBlocked() throws Exception {
        AuthCredentialsRequest authStartRequest = new AuthCredentialsRequest();
        authStartRequest.setLogin(CLIENT_EMAIL_EXISTS_9);
        authStartRequest.setPassword(VALID_PASSWORD);

        AuthNewOtpRequest authNewOtpRequest = new AuthNewOtpRequest();
        authNewOtpRequest.setLogin(CLIENT_EMAIL_EXISTS_9);

        doPost(RestMethods.AUTH_START, authStartRequest)
                .andExpect(isAuthStartedSuccessful());

        BlockedCredentialsEntity blockedCredentialsEntity = new BlockedCredentialsEntity()
                .setLogin(CLIENT_EMAIL_EXISTS_9)
                .setBlockStartDate(LocalDateTime.MIN)
                .setBlockEndDate(LocalDateTime.MAX);
        BlockedCredentialsEntity savedBlockedCredentialsEntity = blockedCredentialsRepository.save(blockedCredentialsEntity);

        doPost(RestMethods.AUTH_NEW_OTP, authNewOtpRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Security.BLOCKED_ERROR));

        blockedCredentialsRepository.delete(savedBlockedCredentialsEntity);
    }

    @Test
    @DisplayName("SUCCESS - new otp")
    public void testAuthNewOtp_success() throws Exception {
        AuthCredentialsRequest authStartRequest = new AuthCredentialsRequest();
        authStartRequest.setLogin(CLIENT_EMAIL_EXISTS_9);
        authStartRequest.setPassword(VALID_PASSWORD);

        AuthNewOtpRequest authNewOtpRequest = new AuthNewOtpRequest();
        authNewOtpRequest.setLogin(CLIENT_EMAIL_EXISTS_9);

        doPost(RestMethods.AUTH_START, authStartRequest)
                .andExpect(isAuthStartedSuccessful());

        doPost(RestMethods.AUTH_NEW_OTP, authNewOtpRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(isAuthStartedSuccessful());
    }

    @Test
    @DisplayName("FAIL - email is blank")
    public void testAuthFinish_emailIsBlank() throws Exception {
        AuthFinishRequest request = new AuthFinishRequest();
        request.setOtp(OTP);

        doPost(RestMethods.AUTH_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - otp is blank")
    public void testAuthFinish_otpIsBlank() throws Exception {
        AuthFinishRequest request = new AuthFinishRequest();
        request.setLogin(CLIENT_EMAIL_EXISTS_10);

        doPost(RestMethods.AUTH_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - email is invalid")
    public void testAuthFinish_emailIsInvalid() throws Exception {
        AuthFinishRequest request = new AuthFinishRequest();
        request.setLogin(INVALID_EMAIL);
        request.setOtp(OTP);

        doPost(RestMethods.AUTH_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - no session")
    public void testAuthFinish_fail_noSession() throws Exception {
        AuthFinishRequest request = new AuthFinishRequest();
        request.setLogin(CLIENT_EMAIL_EXISTS_10);
        request.setOtp(OTP);

        doPost(RestMethods.AUTH_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @Test
    @DisplayName("FAIL - client is blocked")
    public void testAuthFinish_clientIsBlocked() throws Exception {
        AuthCredentialsRequest startRequest = new AuthCredentialsRequest();
        startRequest.setLogin(CLIENT_EMAIL_EXISTS_10);
        startRequest.setPassword(VALID_PASSWORD);

        String sessionId = doPost(RestMethods.AUTH_START, startRequest)
                .andExpect(isAuthStartedSuccessful())
                .andReturn().getResponse().getCookie(SESSION_COOKIE_NAME).getValue();

        BlockedCredentialsEntity blockedCredentialsEntity = new BlockedCredentialsEntity()
                .setLogin(CLIENT_EMAIL_EXISTS_10)
                .setBlockStartDate(LocalDateTime.MIN)
                .setBlockEndDate(LocalDateTime.MAX);
        BlockedCredentialsEntity savedBlockedCredentialsEntity = blockedCredentialsRepository.save(blockedCredentialsEntity);

        AuthFinishRequest finishRequest = new AuthFinishRequest();
        finishRequest.setLogin(CLIENT_EMAIL_EXISTS_10);
        finishRequest.setOtp(OTP);

        doPost(RestMethods.AUTH_FINISH, sessionId, finishRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Security.BLOCKED_ERROR));

        blockedCredentialsRepository.delete(savedBlockedCredentialsEntity);
    }

    @Test
    @DisplayName("SUCCESS - finish auth")
    public void testAuthFinish_success() throws Exception {
        AuthCredentialsRequest startRequest = new AuthCredentialsRequest();
        startRequest.setLogin(CLIENT_EMAIL_EXISTS_11);
        startRequest.setPassword(VALID_PASSWORD);

        String sessionId = doPost(RestMethods.AUTH_START, startRequest)
                .andExpect(isAuthStartedSuccessful())
                .andReturn().getResponse().getCookie(SESSION_COOKIE_NAME).getValue();


        AuthFinishRequest finishRequest = new AuthFinishRequest();
        finishRequest.setLogin(CLIENT_EMAIL_EXISTS_11);
        finishRequest.setOtp(OTP);

        doPost(RestMethods.AUTH_FINISH, sessionId, finishRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));
    }


    private static ResultMatcher isAuthStartedSuccessful() {
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
