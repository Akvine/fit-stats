package ru.akvine.fitstats.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.akvine.fitstats.api.config.RestMethods;
import ru.akvine.fitstats.api.config.TestConfiguration;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthCredentialsRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthFinishRequest;
import ru.akvine.fitstats.entities.security.OtpInfo;
import ru.akvine.fitstats.services.ClientService;
import ru.akvine.fitstats.services.security.AuthActionService;
import ru.akvine.fitstats.services.security.OtpService;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.akvine.fitstats.api.rest.TestConstants.*;

@SpringBootTest(classes = {TestConfiguration.class})
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@AutoConfigureCache
public abstract class ApiBaseTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    private AuthActionService authActionService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private ClientService clientService;

    public static String toJson(final Object obj) {
        try {

            // TODO: подумать над тем, как не создавать постоянно ObjectMapper в статическом контексте
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultActions doPost(String link) throws Exception {
        return doPost(link, null);
    }

    protected ResultActions doPost(String link, String sessionId) throws Exception {
        return doPost(link, sessionId, null);
    }

    protected ResultActions doPost(String link, Object request) throws Exception {
        return doPost(link, null, request);
    }

    protected ResultActions doPost(String link, String sessionId, Object request) throws Exception {
        MockHttpServletRequestBuilder postRequest = post(link)
                .contentType(MediaType.APPLICATION_JSON);

        if (request != null) {
            postRequest.content(toJson(request));
        }
        if (StringUtils.isNotBlank(sessionId)) {
            postRequest.cookie(new Cookie(SESSION_COOKIE_NAME, sessionId));
        }
        return mvc.perform(postRequest);
    }

    protected ResultActions doGet(String link) throws Exception {
        return doGet(link, null);
    }

    protected ResultActions doGet(String link, String sessionId) throws Exception {
        return doGet(link, sessionId, null);
    }

    protected ResultActions doGet(String link, String sessionId, Object request) throws Exception {
        MockHttpServletRequestBuilder getRequest = get(link)
                .contentType(MediaType.APPLICATION_JSON);
        if (request != null) {
            getRequest.content(toJson(request));
        }
        if (StringUtils.isNotBlank(sessionId)) {
            getRequest.cookie(new Cookie(SESSION_COOKIE_NAME, sessionId));
        }
        return mvc.perform(getRequest);
    }

    protected Cookie doAuth(String email) throws Exception {
        clientService.verifyExistsByEmailAndGet(email);

        OtpService mockedOtpService = Mockito.mock(OtpService.class);
        ReflectionTestUtils.setField(this.authActionService, "otpService", mockedOtpService);

        OtpInfo generated = new OtpInfo(3, 600L, 1, OTP);
        Mockito.when(mockedOtpService.getOneTimePassword(email)).thenReturn(generated);

        Cookie session = startAuth(email)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS))
                .andExpect(jsonPath("$.otp.otpNumber").value("1"))
                .andReturn().getResponse().getCookie(SESSION_COOKIE_NAME);

        finishAuth(email, OTP, session.getValue())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(REQUEST_STATUS_SUCCESS));

        ReflectionTestUtils.setField(this.authActionService, "otpService", otpService);
        return session;
    }

    private ResultActions startAuth(String email) throws Exception {
        AuthCredentialsRequest authStartRequest = new AuthCredentialsRequest();
        authStartRequest.setPassword(VALID_PASSWORD);
        authStartRequest.setLogin(email);

        MockHttpServletRequestBuilder requestBuilder =
                post(RestMethods.AUTH_START)
                        .content(toJson(authStartRequest))
                        .contentType(MediaType.APPLICATION_JSON);
        return mvc.perform(requestBuilder);
    }

    private ResultActions finishAuth(String email, String otp, String sessionId) throws Exception {
        AuthFinishRequest request = new AuthFinishRequest();
        request.setLogin(email);
        request.setOtp(otp);

        MockHttpServletRequestBuilder requestBuilder =
                post(RestMethods.AUTH_FINISH)
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON);

        if (StringUtils.isNotBlank(sessionId)) {
            requestBuilder.cookie(new Cookie(SESSION_COOKIE_NAME, sessionId));
        }

        return mvc.perform(requestBuilder);
    }
}
