package ru.akvine.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.akvine.api.config.RestMethods;
import ru.akvine.api.config.TestConfiguration;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientLoginRequest;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = {TestConfiguration.class})
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@AutoConfigureCache
public abstract class ApiBaseTest {
    private static final String SESSION_COOKIE_NAME = "SESSION";

    @Autowired
    protected MockMvc mvc;

    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    protected ResultActions startAuth(String email, String password) throws Exception {
        ClientLoginRequest clientLoginRequest = new ClientLoginRequest()
                .setEmail(email)
                .setPassword(password);

        MockHttpServletRequestBuilder requestBuilder =
                post(RestMethods.REGISTRATION_START)
                .content(toJson(clientLoginRequest))
                .contentType(MediaType.APPLICATION_JSON);
        return mvc.perform(requestBuilder);
    }
}
