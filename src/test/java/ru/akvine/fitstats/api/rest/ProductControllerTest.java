package ru.akvine.fitstats.api.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.akvine.fitstats.api.config.RestMethods;
import ru.akvine.fitstats.controllers.rest.dto.product.AddProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.product.ListProductRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.akvine.fitstats.api.rest.TestConstants.*;

@DisplayName("Product controller")
public class ProductControllerTest extends ApiBaseTest {
    @DisplayName("FAIL - no session")
    @Test
    public void testProductAdd_fail_noSession() throws Exception {
        doPost(RestMethods.PRODUCTS)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.NO_SESSION));
    }

    @DisplayName("FAIL - title is blank")
    @Test
    public void testProductAdd_fail_titleIsBlank() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_12).getValue();

        AddProductRequest request = new AddProductRequest()
                .setProducer("producer")
                .setProteins(3.5)
                .setCarbohydrates(2.5)
                .setFats(1.0)
                .setVol(5D)
                .setVolumeMeasurement("G");

        doPost(RestMethods.PRODUCTS, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @DisplayName("FAIL - producer is blank")
    @Test
    public void testProductAdd_fail_producerIsBlank() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_12).getValue();

        AddProductRequest request = new AddProductRequest()
                .setTitle("title")
                .setProteins(3.5)
                .setCarbohydrates(2.5)
                .setFats(1.0)
                .setVol(5D)
                .setVolumeMeasurement("G");

        doPost(RestMethods.PRODUCTS, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @DisplayName("FAIL - volume measurement is blank")
    @Test
    public void testProductAdd_fail_volumeMeasurementIsBlank() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_12).getValue();

        AddProductRequest request = new AddProductRequest()
                .setTitle("title")
                .setProducer("producer")
                .setProteins(3.5)
                .setCarbohydrates(2.5)
                .setFats(1.0)
                .setVol(5D);

        doPost(RestMethods.PRODUCTS, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR));
    }

    @DisplayName("FAIL - volume measurement is invalid")
    @Test
    public void testProductAdd_fail_volumeMeasurementIsInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_12).getValue();

        AddProductRequest request = new AddProductRequest()
                .setTitle("title")
                .setProducer("producer")
                .setProteins(3.5)
                .setCarbohydrates(2.5)
                .setFats(1.0)
                .setVol(5D)
                .setVolumeMeasurement("invalid measurement");

        doPost(RestMethods.PRODUCTS, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_INVALID_ERROR));
    }

    @DisplayName("SUCCESS - add product")
    @Test
    public void testProductAdd_success() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_12).getValue();

        AddProductRequest request = new AddProductRequest()
                .setTitle("title")
                .setProducer("producer")
                .setProteins(3.5)
                .setCarbohydrates(2.5)
                .setFats(1.0)
                .setVol(5D)
                .setVolumeMeasurement("G");

        doPost(RestMethods.PRODUCTS, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.product.uuid").exists())
                .andExpect(jsonPath("$.product.title").value("title"))
                .andExpect(jsonPath("$.product.producer").value("producer"))
                .andExpect(jsonPath("$.product.proteins").value(3.5))
                .andExpect(jsonPath("$.product.fats").value(1.0))
                .andExpect(jsonPath("$.product.carbohydrates").value(2.5))
                .andExpect(jsonPath("$.product.vol").value(5))
                .andExpect(jsonPath("$.product.volume").value(100))
                .andExpect(jsonPath("$.product.measurement").value("GRAMS"));
    }

    @DisplayName("SUCCESS - list all products")
    @Test
    public void testProductList_success_withoutFilters() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_13).getValue();

        ListProductRequest request = new ListProductRequest();

        doGet(RestMethods.PRODUCTS, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products").isNotEmpty());
    }

    @DisplayName("SUCCESS - list all products by filter")
    @Test
    public void testProductList_success_withFilter() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_13).getValue();

        ListProductRequest request = new ListProductRequest()
                .setFilter("filter");

        doGet(RestMethods.PRODUCTS, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products").isNotEmpty())
                .andExpect(jsonPath("$.products[0]").exists())
                .andExpect(jsonPath("$.products[1]").doesNotExist());
    }

    @DisplayName("FAIL - macronutrient filter invalid")
    @Test
    public void testProductList_fail_macronutrientFilterInvalid() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_13).getValue();

        ListProductRequest request = new ListProductRequest()
                .setMacronutrientsFilter("invalid filter");

        doGet(RestMethods.PRODUCTS, sessionId, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_FAIL))
                .andExpect(jsonPath("$.code").value(CommonErrorCodes.Validation.FIELD_INVALID_ERROR));
    }

    @DisplayName("SUCCESS - list all products by macronutrient filter")
    @Test
    public void testProductList_success_withMacronutrientFilter() throws Exception {
        String sessionId = doAuth(CLIENT_EMAIL_EXISTS_13).getValue();

        ListProductRequest request = new ListProductRequest()
                .setMacronutrientsFilter("fats>3");

        doGet(RestMethods.PRODUCTS, sessionId, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products").isNotEmpty());
    }
}
