package ru.akvine.fitstats.api.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.akvine.fitstats.api.config.RestMethods;
import ru.akvine.fitstats.controllers.rest.dto.weight.ChangeWeightRequest;
import ru.akvine.fitstats.controllers.rest.dto.weight.DeleteWeightRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static ru.akvine.fitstats.api.rest.TestConstants.*;

@DisplayName("Weight controller")
public class WeightControllerTest extends ApiBaseTest {

    @DisplayName("FAIL list - no session")
    @Test
    public void testList_fail_noSession() throws Exception {
        doPost(RestMethods.WEIGHT_RECORD_LIST)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @DisplayName("SUCCESS list")
    @Test
    public void testList_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_4).getValue();

        doPost(RestMethods.WEIGHT_RECORD_LIST, sessionId, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.info").exists())
                .andExpect(jsonPath("$.info").isMap());
    }

    @DisplayName("FAIL change - no session")
    @Test
    public void testChange_fail_noSession() throws Exception {
        doPost(RestMethods.WEIGHT_RECORD_CHANGE)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @DisplayName("FAIL change - invalid number format")
    @Test
    public void testChange_fail_invalidNumberFormat() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_5).getValue();
        ChangeWeightRequest request = new ChangeWeightRequest()
                .setWeight("weight");
        doPost(RestMethods.WEIGHT_RECORD_CHANGE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Weight.WEIGHT_CHANGE_ERROR));
    }

    @DisplayName("FAIL change - number less than 0")
    @Test
    public void testChange_fail_numberLessThanZero() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_5).getValue();
        ChangeWeightRequest request = new ChangeWeightRequest()
                .setWeight("-5");
        doPost(RestMethods.WEIGHT_RECORD_CHANGE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Weight.WEIGHT_INVALID_ERROR));
    }

    @DisplayName("SUCCESS change")
    @Test
    public void testChange_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_5).getValue();
        ChangeWeightRequest request = new ChangeWeightRequest()
                .setWeight("75")
                .setDate(LocalDate.of(2024, 1, 25));
        doPost(RestMethods.WEIGHT_RECORD_CHANGE, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));
    }

    @DisplayName("FAIL delete - no session")
    @Test
    public void testDelete_noSession() throws Exception {
        doPost(RestMethods.WEIGHT_RECORD_DELETE)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @DisplayName("FAIL delete - weight record not found by date")
    @Test
    public void testDelete_weightRecordNotFoundByDate() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_6).getValue();
        DeleteWeightRequest request = new DeleteWeightRequest()
                .setDate(LocalDate.of(135, 10, 1));
        doPost(RestMethods.WEIGHT_RECORD_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Weight.WEIGHT_RECORD_NOT_FOUND_ERROR));
    }

    @DisplayName("SUCCESS delete")
    @Test
    public void testDelete_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_6).getValue();
        DeleteWeightRequest request = new DeleteWeightRequest()
                .setDate(LocalDate.of(2024, 1, 26));
        doPost(RestMethods.WEIGHT_RECORD_DELETE, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));
    }
}
