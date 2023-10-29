package ru.akvine.fitstats.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestSuccessLogoutHandler implements LogoutSuccessHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SuccessfulResponse LOGOUT_SUCCESS_RESPONSE = new SuccessfulResponse();

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().println(objectMapper.writeValueAsString(LOGOUT_SUCCESS_RESPONSE));
    }
}
