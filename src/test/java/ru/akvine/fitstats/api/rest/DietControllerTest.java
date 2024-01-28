package ru.akvine.fitstats.api.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.akvine.fitstats.api.config.RestMethods;
import ru.akvine.fitstats.controllers.rest.dto.diet.*;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DateRangeInfo;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.repositories.DietSettingRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.akvine.fitstats.api.rest.TestConstants.*;

@DisplayName("Diet controller")
public class DietControllerTest extends ApiBaseTest {

    @Autowired
    private DietSettingRepository dietSettingRepository;

    @DisplayName("FAIL - no session")
    @Test
    public void testDietAdd_fail_noSession() throws Exception {
        doPost(RestMethods.DIET_RECORDS_ADD)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @DisplayName("FAIL - product uuid is blank")
    @Test
    public void testDietAdd_fail_productUuidIsBlank() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_14).getValue();
        AddRecordRequest request = new AddRecordRequest();

        doPost(RestMethods.DIET_RECORDS_ADD, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @DisplayName("FAIL - volume less than 0")
    @Test
    public void testDietAdd_fail_volumeLessThanZero() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_14).getValue();
        AddRecordRequest request = new AddRecordRequest()
                .setProductUuid("product_uuid_1")
                .setVolume(-5);

        doPost(RestMethods.DIET_RECORDS_ADD, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Diet.DIET_VOLUME_INVALID_ERROR));
    }

    @DisplayName("SUCCESS - add diet")
    @Test
    public void testDietAdd_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_14).getValue();
        AddRecordRequest request = new AddRecordRequest()
                .setVolume(75)
                .setProductUuid("product_uuid_1");

        doPost(RestMethods.DIET_RECORDS_ADD, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.dietRecord").exists())
                .andExpect(jsonPath("$.dietRecord.uuid").exists())
                .andExpect(jsonPath("$.dietRecord.productUuid").value("product_uuid_1"))
                .andExpect(jsonPath("$.dietRecord.productTitle").value("title_1"))
                .andExpect(jsonPath("$.dietRecord.measurement").value("GRAMS"));
    }

    @DisplayName("SUCCESS - list records")
    @Test
    public void testDietList_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_15).getValue();
        ListRecordRequest request = new ListRecordRequest()
                .setDate(LocalDate.of(2024, 1, 27));

        doGet(RestMethods.DIET_RECORDS_LIST, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.records").isNotEmpty());
    }

    @DisplayName("SUCCESS - list records by date")
    @Test
    public void testDietList_success_byDate() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_15).getValue();
        ListRecordRequest request = new ListRecordRequest()
                .setDate(LocalDate.of(2024, 1, 28));

        doGet(RestMethods.DIET_RECORDS_LIST, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.records").isNotEmpty())
                .andExpect(jsonPath("$.records[0]").exists())
                .andExpect(jsonPath("$.records[1]").exists());
    }

    @DisplayName("FAIL - record doesn't find by uuid")
    @Test
    public void testDietDelete_fail_invalidUuid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_16).getValue();
        DeleteRecordRequest request = new DeleteRecordRequest()
                .setUuid("very long invalid record uuid for imitation real uuid");

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Diet.DIET_RECORD_NOT_FOUND_ERROR));
    }

    @DisplayName("FAIL - record doesn't find by short uuid")
    @Test
    public void testDietDelete_fail_invalidShortUuid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_16).getValue();
        DeleteRecordRequest request = new DeleteRecordRequest()
                .setUuid("invalid short uuid");

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Diet.DIET_RECORDS_NOT_UNIQUE_RESULT_ERROR));
    }

    @DisplayName("SUCCESS - delete record")
    @Test
    public void testDietDelete_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_16).getValue();
        DeleteRecordRequest request = new DeleteRecordRequest()
                .setUuid("diet_record_uuid_4");

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));
    }

    @DisplayName("FAIL - date range has empty fields")
    @Test
    public void testDietDeleteByDateRange_fail_dateRangeHasEmptyFields() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_17).getValue();
        DateRangeInfo dateRangeInfo = new DateRangeInfo();
        DeleteRecordRequest request = (DeleteRecordRequest) new DeleteRecordRequest()
                .setDateRangeInfo(dateRangeInfo);

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Statistic.DATE_RANGE_VALUES_EMPTY));
    }

    @DisplayName("FAIL - start date is null")
    @Test
    public void testDietDeleteByDateRange_fail_startDateIsNull() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_17).getValue();
        DateRangeInfo dateRangeInfo = new DateRangeInfo()
                .setStartDate(LocalDate.of(1900, 1, 1));
        DeleteRecordRequest request = (DeleteRecordRequest) new DeleteRecordRequest()
                .setDateRangeInfo(dateRangeInfo);

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Statistic.DATE_RANGE_VALUES_EMPTY));
    }

    @DisplayName("FAIL - end date is null")
    @Test
    public void testDietDeleteByDateRange_fail_endDateIsNull() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_17).getValue();
        DateRangeInfo dateRangeInfo = new DateRangeInfo()
                .setEndDate(LocalDate.of(1900, 1, 1));
        DeleteRecordRequest request = (DeleteRecordRequest) new DeleteRecordRequest()
                .setDateRangeInfo(dateRangeInfo);

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Statistic.DATE_RANGE_VALUES_EMPTY));
    }

    @DisplayName("FAIL - start date is after end date")
    @Test
    public void testDietDeleteByDateRange_fail_startDateIsAfterEndDate() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_17).getValue();
        DateRangeInfo dateRangeInfo = new DateRangeInfo()
                .setStartDate(LocalDate.of(1900, 1, 1))
                .setEndDate(LocalDate.of(1899, 1, 1));
        DeleteRecordRequest request = (DeleteRecordRequest) new DeleteRecordRequest()
                .setDateRangeInfo(dateRangeInfo);

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Statistic.START_DATE_AFTER_END_DATE_ERROR));
    }

    @DisplayName("FAIL - date range has all fields values")
    @Test
    public void testDietDeleteByDateRange_fail_hasAllFieldsValue() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_17).getValue();
        DateRangeInfo dateRangeInfo = new DateRangeInfo()
                .setStartDate(LocalDate.of(1900, 1, 1))
                .setEndDate(LocalDate.of(2999, 1, 1))
                .setDuration(Duration.YEAR.name());
        DeleteRecordRequest request = (DeleteRecordRequest) new DeleteRecordRequest()
                .setDateRangeInfo(dateRangeInfo);

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Statistic.ILLEGAL_DATE_RANGE_STATE_ERROR));
    }

    @DisplayName("FAIL - duration is invalid")
    @Test
    public void testDietDeleteByDateRange_fail_durationIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_17).getValue();
        DateRangeInfo dateRangeInfo = new DateRangeInfo()
                .setDuration("invalid duration");
        DeleteRecordRequest request = (DeleteRecordRequest) new DeleteRecordRequest()
                .setDateRangeInfo(dateRangeInfo);

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Statistic.DURATION_INVALID_ERROR));
    }

    @DisplayName("SUCCESS - delete by date range")
    @Test
    public void testDietDeleteByDateRange_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_17).getValue();
        DateRangeInfo dateRangeInfo = new DateRangeInfo()
                .setDuration(Duration.YEAR.name());
        DeleteRecordRequest request = (DeleteRecordRequest) new DeleteRecordRequest()
                .setDateRangeInfo(dateRangeInfo);

        doPost(RestMethods.DIET_RECORDS_DELETE, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));
    }

    @DisplayName("FAIL - no session")
    @Test
    public void testDietDisplay_fail_noSession() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_18).getValue();

        doPost(RestMethods.DIET_DISPLAY, sessionId)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @DisplayName("SUCCESS - diet display without records")
    @Test
    public void testDietDisplay_success_withoutRecords() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_18).getValue();

        doGet(RestMethods.DIET_DISPLAY, sessionId, new DisplayRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.dietDisplayInfo.maxProteins").value("50"))
                .andExpect(jsonPath("$.dietDisplayInfo.maxCarbohydrates").value("50"))
                .andExpect(jsonPath("$.dietDisplayInfo.maxFats").value("50"))
                .andExpect(jsonPath("$.dietDisplayInfo.maxCalories").value("850"))
                .andExpect(jsonPath("$.dietDisplayInfo.currentProteins").value("0"))
                .andExpect(jsonPath("$.dietDisplayInfo.currentFats").value("0"))
                .andExpect(jsonPath("$.dietDisplayInfo.currentCarbohydrates").value("0"))
                .andExpect(jsonPath("$.dietDisplayInfo.currentCalories").value("0"))
                .andExpect(jsonPath("$.dietDisplayInfo.remainingProteins").value("50"))
                .andExpect(jsonPath("$.dietDisplayInfo.remainingFats").value("50"))
                .andExpect(jsonPath("$.dietDisplayInfo.remainingCarbohydrates").value("50"))
                .andExpect(jsonPath("$.dietDisplayInfo.remainingCalories").value("850"));
    }

    @DisplayName("SUCCESS - diet display with records")
    @Test
    public void testDietDisplay_success_withRecords() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_19).getValue();
        DisplayRequest request = new DisplayRequest()
                .setDate(LocalDate.of(2024, 1, 27));

        doGet(RestMethods.DIET_DISPLAY, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.dietDisplayInfo.maxProteins").value("50"))
                .andExpect(jsonPath("$.dietDisplayInfo.maxCarbohydrates").value("50"))
                .andExpect(jsonPath("$.dietDisplayInfo.maxFats").value("50"))
                .andExpect(jsonPath("$.dietDisplayInfo.maxCalories").value("850"))
                .andExpect(jsonPath("$.dietDisplayInfo.currentProteins").value("10"))
                .andExpect(jsonPath("$.dietDisplayInfo.currentFats").value("20"))
                .andExpect(jsonPath("$.dietDisplayInfo.currentCarbohydrates").value("15"))
                .andExpect(jsonPath("$.dietDisplayInfo.currentCalories").value("410"))
                .andExpect(jsonPath("$.dietDisplayInfo.remainingProteins").value("40"))
                .andExpect(jsonPath("$.dietDisplayInfo.remainingFats").value("30"))
                .andExpect(jsonPath("$.dietDisplayInfo.remainingCarbohydrates").value("35"))
                .andExpect(jsonPath("$.dietDisplayInfo.remainingCalories").value("440"));
    }

    @DisplayName("FAIL - no session")
    @Test
    public void testDietChangeDiet_fail_noSession() throws Exception {
        doPost(RestMethods.DIET_CHANGE)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @DisplayName("FAIL - diet is invalid")
    @Test
    public void testDietChangeDiet_fail_dietIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_20).getValue();
        ChangeDietRequest request = new ChangeDietRequest()
                .setDietType("Invalid diet");

        doPost(RestMethods.DIET_CHANGE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Biometric.DIET_INVALID_ERROR));
    }

    @DisplayName("SUCCESS - change diet")
    @Test
    public void testDietChangeDiet_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_20).getValue();
        ChangeDietRequest request = new ChangeDietRequest()
                .setDietType(Diet.GAIN.name());

        doPost(RestMethods.DIET_CHANGE, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));

        DietSettingEntity dietSettingEntity = dietSettingRepository.findByClientUuid(CLIENT_UUID_20).get();
        assertThat(dietSettingEntity.getDiet()).isEqualTo(Diet.GAIN);
    }
}
