package ru.akvine.api.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.akvine.api.config.RestMethods;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationCheckOtpRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationNewOtpRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationStartRequest;
import ru.akvine.fitstats.enums.*;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.akvine.api.rest.TestConstants.*;

@DisplayName("Registration start")
public class RegistrationControllerTest extends ApiBaseTest {

    @Test
    @DisplayName("FAIL - invalid email")
    public void testRegistrationStart_fail_invalidEmail() throws Exception {
        RegistrationStartRequest request = (RegistrationStartRequest) new RegistrationStartRequest()
                .setLogin(INVALID_EMAIL);

        doPost(RestMethods.REGISTRATION_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - client already exists with email")
    public void testRegistrationStart_fail_clientAlreadyExists() throws Exception {
        RegistrationStartRequest request = (RegistrationStartRequest) new RegistrationStartRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_1);

        doPost(RestMethods.REGISTRATION_START, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Client.CLIENT_ALREADY_EXISTS_ERROR));
    }

    @Test
    @DisplayName("SUCCESS - registration client")
    public void testRegistrationStart_success() throws Exception {
        RegistrationStartRequest request = (RegistrationStartRequest) new RegistrationStartRequest()
                .setLogin(CLIENT_EMAIL_NOT_EXISTS_1);

        doPost(RestMethods.REGISTRATION_START, request)
                .andExpect(isRegistrationStartedSuccessful());
    }

    @Test
    @DisplayName("FAIL - invalid email")
    public void testRegistrationCheck_fail_invalidEmail() throws Exception {
        RegistrationCheckOtpRequest request = (RegistrationCheckOtpRequest) new RegistrationCheckOtpRequest()
                .setLogin(INVALID_EMAIL);
        request.setOtp(OTP);

        doPost(RestMethods.REGISTRATION_CHECK, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - client already exists with email")
    public void testRegistrationCheck_fail_clientAlreadyExists() throws Exception {
        RegistrationCheckOtpRequest request = (RegistrationCheckOtpRequest) new RegistrationCheckOtpRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_3);
        request.setOtp(OTP);

        doPost(RestMethods.REGISTRATION_CHECK, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Client.CLIENT_ALREADY_EXISTS_ERROR));
    }

    @Test
    @DisplayName("SUCCESS - check otp")
    public void testRegistrationCheck_success() throws Exception {
        RegistrationStartRequest startRequest = (RegistrationStartRequest) new RegistrationStartRequest()
                .setLogin(CLIENT_EMAIL_NOT_EXISTS_2);

        Cookie session = doPost(RestMethods.REGISTRATION_START, startRequest)
                .andExpect(isRegistrationStartedSuccessful())
                .andReturn().getResponse().getCookie(SESSION_COOKIE_NAME);
        String sessionId = session.getValue();

        RegistrationCheckOtpRequest checkOtpRequest = (RegistrationCheckOtpRequest) new RegistrationCheckOtpRequest()
                .setLogin(CLIENT_EMAIL_NOT_EXISTS_2);
        checkOtpRequest.setOtp(OTP);

        doPost(RestMethods.REGISTRATION_CHECK, sessionId, checkOtpRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.state").value(OTP_PASSED_STATE));
    }

    @Test
    @DisplayName("FAIL - invalid email")
    public void testRegistrationNewOtp_fail_invalidEmail() throws Exception {
        RegistrationNewOtpRequest request = (RegistrationNewOtpRequest) new RegistrationNewOtpRequest()
                .setLogin(INVALID_EMAIL);

        doPost(RestMethods.REGISTRATION_NEWOTP, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Client.EMAIL_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - client already exists with email")
    public void testRegistrationNewOtp_fail_clientAlreadyExists() throws Exception {
        RegistrationStartRequest request = (RegistrationStartRequest) new RegistrationStartRequest()
                .setLogin(CLIENT_EMAIL_EXISTS_2);

        doPost(RestMethods.REGISTRATION_NEWOTP, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Client.CLIENT_ALREADY_EXISTS_ERROR));
    }

    @Test
    @DisplayName("FAIL - age less than 0")
    public void testRegistrationFinish_fail_ageLessThanZero() throws Exception {
        RegistrationFinishRequest request = buildDefaultFinishRequest(CLIENT_EMAIL_NOT_EXISTS_3)
                .setAge(-15);

        doPost(RestMethods.REGISTRATION_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Biometric.AGE_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - height is not number")
    public void testRegistrationFinish_fail_heightIsNotNumber() throws Exception {
        RegistrationFinishRequest request = buildDefaultFinishRequest(CLIENT_EMAIL_NOT_EXISTS_3)
                .setHeight("not a number");

        doPost(RestMethods.REGISTRATION_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Biometric.FIELD_NUMBER_INVALID));
    }

    @Test
    @DisplayName("FAIL - weight is not number")
    public void testRegistrationFinish_fail_weightIsNotNumber() throws Exception {
        RegistrationFinishRequest request = buildDefaultFinishRequest(CLIENT_EMAIL_NOT_EXISTS_3)
                .setHeight("not a number");

        doPost(RestMethods.REGISTRATION_FINISH, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Biometric.FIELD_NUMBER_INVALID));
    }

    @Test
    @DisplayName("SUCCESS - finish registration")
    public void testRegistrationFinish_success() throws Exception {
        RegistrationStartRequest startRequest = (RegistrationStartRequest) new RegistrationStartRequest()
                .setLogin(CLIENT_EMAIL_NOT_EXISTS_3);

        Cookie session = doPost(RestMethods.REGISTRATION_START, startRequest)
                .andExpect(isRegistrationStartedSuccessful())
                .andReturn().getResponse().getCookie(SESSION_COOKIE_NAME);
        String sessionId = session.getValue();

        RegistrationCheckOtpRequest checkOtpRequest = (RegistrationCheckOtpRequest) new RegistrationCheckOtpRequest()
                .setLogin(CLIENT_EMAIL_NOT_EXISTS_3);
        checkOtpRequest.setOtp(OTP);

        doPost(RestMethods.REGISTRATION_CHECK, sessionId, checkOtpRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.state").value(OTP_PASSED_STATE));

        RegistrationFinishRequest finishRequest = buildDefaultFinishRequest(CLIENT_EMAIL_NOT_EXISTS_3);

        doPost(RestMethods.REGISTRATION_FINISH, sessionId, finishRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));
    }

    public RegistrationFinishRequest buildDefaultFinishRequest(String email) {
        String fistName = "First name";
        String secondName = "Second name";
        String thirdName = "Third name";
        String height = "170";
        String weight = "70";
        int age = 20;
        return (RegistrationFinishRequest) new RegistrationFinishRequest()
                .setAge(age)
                .setFirstName(fistName)
                .setSecondName(secondName)
                .setThirdName(thirdName)
                .setHeight(height)
                .setWeight(weight)
                .setPhysicalActivity(PhysicalActivity.EXTREMELY_ACTIVE.toString())
                .setHeightMeasurement(HeightMeasurement.CM.toString())
                .setWeightMeasurement(WeightMeasurement.KG.toString())
                .setGender(Gender.MALE.toString())
                .setDiet(Diet.GAIN.toString())
                .setPassword(VALID_PASSWORD)
                .setLogin(email);
    }

    private static ResultMatcher isRegistrationStartedSuccessful() {
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
