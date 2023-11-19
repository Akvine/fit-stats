package ru.akvine.fitstats.controllers.rest.converter.security;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.security.OtpActionResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthCredentialsRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthResponse;
import ru.akvine.fitstats.services.dto.security.auth.AuthActionRequest;
import ru.akvine.fitstats.services.dto.security.auth.AuthActionResult;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthConverter {

    @Value("${security.otp.new.delay.seconds}")
    private int otpNewDelaySeconds;

    public AuthActionRequest convertToAuthActionRequest(AuthCredentialsRequest request, HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "authCredentialsRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");
        return new AuthActionRequest()
                .setLogin(request.getLogin())
                .setSessionId(httpServletRequest.getSession(true).getId())
                .setPassword(StringUtils.trimToEmpty(request.getPassword()));
    }

    public AuthActionRequest convertToAuthActionRequest(AuthFinishRequest request, HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "authFinishRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");

        return new AuthActionRequest()
                .setLogin(request.getLogin())
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId())
                .setOtp(request.getOtp());
    }

    public AuthResponse convertToAuthResponse(AuthActionResult result) {
        Preconditions.checkNotNull(result, "authActionResult is null");
        Preconditions.checkNotNull(result.getOtp(), "authActionResult.otp is null");

        OtpActionResponse otpActionResponse = new OtpActionResponse()
                .setActionExpiredAt(result.getOtp().getExpiredAt().toString())
                .setOtpNumber(result.getOtp().getOtpNumber())
                .setOtpCountLeft(result.getOtp().getOtpCountLeft())
                .setOtpLastUpdate(result.getOtp().getOtpLastUpdate().toString())
                .setNewOtpDelay(otpNewDelaySeconds)
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());
        return new AuthResponse().setOtp(otpActionResponse);
    }

}
