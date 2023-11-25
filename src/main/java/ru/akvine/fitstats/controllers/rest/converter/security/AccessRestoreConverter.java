package ru.akvine.fitstats.controllers.rest.converter.security;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.security.OtpActionResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreCheckOtpRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreStartRequest;
import ru.akvine.fitstats.services.dto.security.access_restore.AccessRestoreActionRequest;
import ru.akvine.fitstats.services.dto.security.access_restore.AccessRestoreActionResult;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccessRestoreConverter {
    @Value("${security.otp.new.delay.seconds}")
    private int otpNewDelaySeconds;

    public AccessRestoreActionRequest convertToAccessRestoreActionRequest(AccessRestoreStartRequest request,
                                                                          HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "accessRestoreStartRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");
        return new AccessRestoreActionRequest()
                .setLogin(request.getLogin())
                .setSessionId(httpServletRequest.getSession(true).getId());
    }

    public AccessRestoreActionRequest convertToAccessRestoreActionRequest(AccessRestoreCheckOtpRequest request,
                                                                          HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "accessRestoreCheckOtpRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");

        return new AccessRestoreActionRequest()
                .setLogin(request.getLogin())
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId())
                .setOtp(request.getOtp());
    }

    public AccessRestoreActionRequest convertToAccessRestoreActionRequest(AccessRestoreFinishRequest request,
                                                                          HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "accessRestoreFinishRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");
        return new AccessRestoreActionRequest()
                .setLogin(request.getLogin())
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId())
                .setPassword(request.getPassword());
    }

    public AccessRestoreResponse convertToAccessRestoreResponse(AccessRestoreActionResult result) {
        Preconditions.checkNotNull(result, "accessRestoreActionResult is null");
        Preconditions.checkNotNull(result.getState(), "accessRestoreActionResult.state is null");
        Preconditions.checkNotNull(result.getOtp(), "accessRestoreActionResult.otp is null");

        OtpActionResponse otpActionResponse = new OtpActionResponse()
                .setActionExpiredAt(result.getOtp().getExpiredAt().toString())
                .setOtpCountLeft(result.getOtp().getOtpCountLeft())
                .setOtpNumber(result.getOtp().getOtpNumber())
                .setOtpLastUpdate(result.getOtp().getOtpLastUpdate() != null ? result.getOtp().getOtpLastUpdate().toString() : null)
                .setNewOtpDelay(otpNewDelaySeconds)
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());

        return new AccessRestoreResponse()
                .setState(result.getState().toString())
                .setOtp(otpActionResponse);
    }
}
