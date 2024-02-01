package ru.akvine.fitstats.controllers.rest.converter.security;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.security.OtpActionResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.*;
import ru.akvine.fitstats.enums.*;
import ru.akvine.fitstats.services.dto.security.registration.RegistrationActionRequest;
import ru.akvine.fitstats.services.dto.security.registration.RegistrationActionResult;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class RegistrationConverter {
    private final PropertyParseService propertyParseService;

    @Value("security.otp.new.delay.seconds")
    private String otpNewDelaySeconds;

    public RegistrationActionRequest convertToRegistrationActionRequest(RegistrationStartRequest request,
                                                                        HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "registrationStartRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");
        return new RegistrationActionRequest()
                .setLogin(request.getLogin())
                .setSessionId(httpServletRequest.getSession(true).getId());
    }

    public RegistrationActionRequest convertToRegistrationActionRequest(RegistrationCheckOtpRequest request,
                                                                        HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "registrationCheckOtpRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");

        return new RegistrationActionRequest()
                .setLogin(request.getLogin())
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId())
                .setOtp(request.getOtp());
    }

    public RegistrationActionRequest convertToRegistrationActionRequest(RegistrationNewOtpRequest request,
                                                                        HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "registrationNewOtpRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");
        return new RegistrationActionRequest()
                .setLogin(request.getLogin())
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId());
    }

    public RegistrationActionRequest convertToRegistrationActionRequest(RegistrationFinishRequest request,
                                                                        HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "registrationFinishRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");

        return new RegistrationActionRequest()
                .setLogin(request.getLogin())
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId())
                .setPassword(request.getPassword())
                .setFirstName(request.getFirstName())
                .setSecondName(request.getSecondName())
                .setGender(Gender.valueOf(request.getGender()))
                .setAge(request.getAge())
                .setPhysicalActivity(PhysicalActivity.valueOf(request.getPhysicalActivity()))
                .setDiet(Diet.valueOf(request.getDiet()))
                .setThirdName(request.getThirdName())
                .setHeight(request.getHeight())
                .setHeightMeasurement(HeightMeasurement.valueOf(request.getHeightMeasurement()))
                .setWeight(request.getWeight())
                .setWeightMeasurement(WeightMeasurement.valueOf(request.getWeightMeasurement()));
    }

    public RegistrationResponse convertToRegistrationResponse(RegistrationActionResult result) {
        Preconditions.checkNotNull(result, "registrationActionResult is null");

        OtpActionResponse otpActionResponse = new OtpActionResponse()
                .setActionExpiredAt(result.getOtp().getExpiredAt().toString())
                .setOtpCountLeft(result.getOtp().getOtpCountLeft())
                .setOtpNumber(result.getOtp().getOtpNumber())
                .setOtpLastUpdate(result.getOtp().getOtpLastUpdate() != null ? result.getOtp().getOtpLastUpdate().toString() : null)
                .setNewOtpDelay(propertyParseService.parseLong(otpNewDelaySeconds))
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());

        return new RegistrationResponse()
                .setOtp(otpActionResponse)
                .setState(result.getState().name());
    }
}
