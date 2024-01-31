package ru.akvine.fitstats.api.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.akvine.fitstats.api.config.RestMethods;
import ru.akvine.fitstats.controllers.rest.dto.admin.DeleteProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.ExportProductsRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.UpdateProductRequest;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.akvine.fitstats.api.rest.TestConstants.*;

@DisplayName("Admin controller")
public class AdminControllerTest extends ApiBaseTest {
    @Test
    @DisplayName("FAIL - no session")
    public void testProductExport_fail_noSession() throws Exception {
        ExportProductsRequest request = new ExportProductsRequest();

        doPost(RestMethods.ADMIN_PRODUCTS_EXPORT, request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @Test
    @DisplayName("FAIL - secret is blank")
    public void testProductsExport_fail_secretIsBlank() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        ExportProductsRequest request = new ExportProductsRequest();

        doPost(RestMethods.ADMIN_PRODUCTS_EXPORT, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }


    @Test
    @DisplayName("FAIL - secret is invalid")
    public void testProductExport_fail_secretIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        ExportProductsRequest request = (ExportProductsRequest) new ExportProductsRequest()
                .setSecret("invalid secret");

        doPost(RestMethods.ADMIN_PRODUCTS_EXPORT, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Security.BAD_CREDENTIALS_ERROR));
    }

    @Test
    @DisplayName("FAIL - invalid converter type")
    public void testProductExport_fail_converterTypeIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        ExportProductsRequest request = (ExportProductsRequest) new ExportProductsRequest()
                .setConverterType("convert type invalid")
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_EXPORT, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR));
    }

    @Test
    @DisplayName("SUCCESS - export")
    public void testProductExport_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        ExportProductsRequest request = (ExportProductsRequest) new ExportProductsRequest()
                .setConverterType(ConverterType.CSV.name())
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_EXPORT, sessionId, request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("FAIL - no session")
    public void testProductUpdate_noSession() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest();

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @Test
    @DisplayName("FAIL - secret is blank")
    public void testProductsUpdate_fail_secretIsBlank() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        UpdateProductRequest request = new UpdateProductRequest();

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }


    @Test
    @DisplayName("FAIL - secret is invalid")
    public void testProductUpdate_fail_secretIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        UpdateProductRequest request = (UpdateProductRequest) new UpdateProductRequest()
                .setUuid(PRODUCT_UUID_3)
                .setSecret("invalid secret");

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Security.BAD_CREDENTIALS_ERROR));
    }

    @Test
    @DisplayName("FAIL - uuid is blank")
    public void testProductUpdate_fail_uuidIsBlank() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        UpdateProductRequest request = (UpdateProductRequest) new UpdateProductRequest()
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - volume measurement is invalid")
    public void testProductUpdate_fail_volumeMeasurementIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        UpdateProductRequest request = (UpdateProductRequest) new UpdateProductRequest()
                .setUuid(PRODUCT_UUID_3)
                .setMeasurement("invalid measurement")
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - vol is invalid")
    public void testProductUpdate_fail_volIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        UpdateProductRequest request = (UpdateProductRequest) new UpdateProductRequest()
                .setUuid(PRODUCT_UUID_3)
                .setVol(-15D)
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - proteins is invalid")
    public void testProductUpdate_fail_proteinsIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        UpdateProductRequest request = (UpdateProductRequest) new UpdateProductRequest()
                .setUuid(PRODUCT_UUID_3)
                .setProteins(-15D)
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - carbohydrates is invalid")
    public void testProductUpdate_fail_carbohydratesIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        UpdateProductRequest request = (UpdateProductRequest) new UpdateProductRequest()
                .setUuid(PRODUCT_UUID_3)
                .setCarbohydrates(-15D)
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - fats is invalid")
    public void testProductUpdate_fail_fatsIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        UpdateProductRequest request = (UpdateProductRequest) new UpdateProductRequest()
                .setUuid(PRODUCT_UUID_3)
                .setFats(-15D)
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR));
    }

    @Test
    @DisplayName("FAIL - fats is invalid")
    public void testProductUpdate_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        UpdateProductRequest request = (UpdateProductRequest) new UpdateProductRequest()
                .setUuid(PRODUCT_UUID_3)
                .setVol(10D)
                .setProteins(90D)
                .setTitle("new title")
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_UPDATE, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.product.uuid").value(PRODUCT_UUID_3))
                .andExpect(jsonPath("$.product.vol").value(10))
                .andExpect(jsonPath("$.product.title").value("new title"))
                .andExpect(jsonPath("$.product.proteins").value(90));
    }

    @Test
    @DisplayName("FAIL - no session")
    public void testProductDelete_fail_noSession() throws Exception {
        DeleteProductRequest request = new DeleteProductRequest();

        doPost(RestMethods.ADMIN_PRODUCTS_DELETE, request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @Test
    @DisplayName("FAIL - secret is blank")
    public void testProductDelete_fail_secretIsBlank() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        DeleteProductRequest request = new DeleteProductRequest()
                .setUuid(PRODUCT_UUID_4);

        doPost(RestMethods.ADMIN_PRODUCTS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - uuid is blank")
    public void testProductDelete_fail_uuidIsBlank() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        DeleteProductRequest request = (DeleteProductRequest) new DeleteProductRequest()
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @Test
    @DisplayName("FAIL - product not found")
    public void testProductDelete_fail_productNotFound() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        DeleteProductRequest request = (DeleteProductRequest) new DeleteProductRequest()
                .setUuid("product not found")
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_DELETE, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Product.PRODUCT_NOT_FOUND_ERROR));
    }

    @Test
    @DisplayName("FAIL - success")
    public void testProductDelete_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_30).getValue();
        DeleteProductRequest request = (DeleteProductRequest) new DeleteProductRequest()
                .setUuid(PRODUCT_UUID_4)
                .setSecret(SECRET);

        doPost(RestMethods.ADMIN_PRODUCTS_DELETE, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));
    }
}
