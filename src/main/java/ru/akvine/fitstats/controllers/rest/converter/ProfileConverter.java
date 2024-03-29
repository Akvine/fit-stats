package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.dto.profile.*;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_email.ProfileChangeEmailFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_email.ProfileChangeEmailStartRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_password.ProfileChangePasswordFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_password.ProfileChangePasswordStartRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.delete.ProfileDeleteFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.delete.ProfileDeleteResponse;
import ru.akvine.fitstats.controllers.rest.dto.profile.file.DietRecordCsvRow;
import ru.akvine.fitstats.controllers.rest.dto.profile.file.DietRecordXlsxRow;
import ru.akvine.fitstats.controllers.rest.dto.security.OtpActionResponse;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;
import ru.akvine.fitstats.managers.ParsersManager;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.services.dto.profile.UpdateSettings;
import ru.akvine.fitstats.services.dto.profile.change_email.ProfileChangeEmailActionRequest;
import ru.akvine.fitstats.services.dto.profile.change_email.ProfileChangeEmailActionResult;
import ru.akvine.fitstats.services.dto.profile.change_email.ProfileChangeEmailResponse;
import ru.akvine.fitstats.services.dto.profile.change_password.ProfileChangePasswordActionRequest;
import ru.akvine.fitstats.services.dto.profile.change_password.ProfileChangePasswordActionResult;
import ru.akvine.fitstats.services.dto.profile.change_password.ProfileChangePasswordResponse;
import ru.akvine.fitstats.services.dto.profile.delete.ProfileDeleteActionRequest;
import ru.akvine.fitstats.services.dto.profile.delete.ProfileDeleteActionResult;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProfileConverter {
    private static final String HEADER_PREFIX = "attachment; filename=";
    private static final String DEFAULT_FILE_NAME = "file";
    private static final String POINT = ".";

    private final ParsersManager parsersManager;
    private final PropertyParseService propertyParseService;

    @Value("security.otp.new.delay.seconds")
    private String otpNewDelaySeconds;

    public ProfileDownload convertToProfileDownload(
            LocalDate startDate,
            LocalDate endDate,
            String duration,
            String converterType) {
        return new ProfileDownload()
                .setConverterType(converterType == null ? ConverterType.CSV : ConverterType.valueOf(converterType.toUpperCase()))
                .setDuration(StringUtils.isBlank(duration) ? null : Duration.valueOf(duration))
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid());
    }

    public ResponseEntity convertToExportResponse(byte[] file,
                                                  String filename,
                                                  ConverterType converterType) {
        Preconditions.checkNotNull(file, "file is null");
        Preconditions.checkNotNull(converterType, "converterType is null");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, resolveHeaderType(filename, converterType))
                .contentType(MediaType.parseMediaType(resolveMediaType(converterType)))
                .body(file);
    }

    public ImportRecords convertToImportRecords(String converterType, MultipartFile file) {
        ConverterType type = ConverterType.valueOf(converterType);
        return new ImportRecords()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setRecords(parsersManager
                        .getParsers()
                        .get(type)
                        .parse(file, resolveClass(type)));
    }

    public UpdateBiometric convertToUpdateBiometric(UpdateBiometricRequest request) {
        Preconditions.checkNotNull(request, "updateBiometricRequest is null");
        return new UpdateBiometric()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setUpdateDietSetting(request.isUpdateDietSetting())
                .setAge(request.getAge())
                .setHeight(request.getHeight())
                .setWeight(request.getWeight())
                .setPhysicalActivity(request.getPhysicalActivity() != null ?
                        PhysicalActivity.valueOf(request.getPhysicalActivity()) : null);
    }

    public UpdateBiometricResponse convertToUpdateBiometricResponse(BiometricBean biometricBean) {
        Preconditions.checkNotNull(biometricBean, "biometricBean is null");
        return new UpdateBiometricResponse()
                .setAge(biometricBean.getAge())
                .setHeight(biometricBean.getHeight())
                .setWeight(biometricBean.getWeight())
                .setPhysicalActivity(biometricBean.getPhysicalActivity().name())
                .setHeightMeasurement(biometricBean.getHeightMeasurement().name())
                .setWeightMeasurement(biometricBean.getWeightMeasurement().name());
    }

    public DisplayBiometricResponse convertToDisplayBiometricResponse(BiometricBean biometricBean) {
        Preconditions.checkNotNull(biometricBean, "biometricBean is null");
        return new DisplayBiometricResponse()
                .setAge(biometricBean.getAge())
                .setHeight(biometricBean.getHeight())
                .setWeight(biometricBean.getWeight())
                .setPhysicalActivity(biometricBean.getPhysicalActivity().name())
                .setHeightMeasurement(biometricBean.getHeightMeasurement().name())
                .setWeightMeasurement(biometricBean.getWeightMeasurement().name());
    }

    public ProfileDeleteActionRequest convertToProfileDeleteActionRequest(HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(httpServletRequest, "httpSevletRequest is null");
        return new ProfileDeleteActionRequest()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId());
    }

    public ProfileDeleteActionRequest convertToProfileDeleteActionRequest(ProfileDeleteFinishRequest request,
                                                                          HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "profileDeleteFinishRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");
        return new ProfileDeleteActionRequest()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId())
                .setOtp(request.getOtp());
    }

    public ProfileDeleteResponse convertToProfileDeleteResponse(ProfileDeleteActionResult result) {
        Preconditions.checkNotNull(result, "profileDeleteActionResult is null");
        Preconditions.checkNotNull(result.getOtp(), "profileDeleteActionResult.otp is null");

        OtpActionResponse otpActionResponse = new OtpActionResponse()
                .setActionExpiredAt(result.getOtp().getExpiredAt().toString())
                .setOtpNumber(result.getOtp().getOtpNumber())
                .setOtpCountLeft(result.getOtp().getOtpCountLeft())
                .setOtpLastUpdate(result.getOtp().getOtpLastUpdate().toString())
                .setNewOtpDelay(propertyParseService.parseLong(otpNewDelaySeconds))
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());

        return new ProfileDeleteResponse()
                .setOtp(otpActionResponse)
                .setPwdInvalidAttemptsLeft(result.getPwdInvalidAttemptsLeft());
    }

    public ProfileChangeEmailActionRequest convertToProfileChangeEmailActionRequest(ProfileChangeEmailStartRequest request,
                                                                                    HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "profileChangeEmailStartRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");
        return new ProfileChangeEmailActionRequest()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setNewEmail(request.getNewEmail().trim())
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId());
    }

    public ProfileChangeEmailActionRequest convertToProfileChangeEmailActionRequest(ProfileChangeEmailFinishRequest request,
                                                                                    HttpServletRequest httpServletRequest) {
        Preconditions.checkNotNull(request, "profileChangeEmailFinishRequest is null");
        Preconditions.checkNotNull(httpServletRequest, "httpServletRequest is null");
        return new ProfileChangeEmailActionRequest()
                .setSessionId(SecurityUtils.getSession(httpServletRequest).getId())
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setOtp(request.getOtp());
    }

    public ProfileChangeEmailResponse convertToProfileChangeEmailResponse(ProfileChangeEmailActionResult result) {
        Preconditions.checkNotNull(result, "profileChangeEmailActionResult is null");
        Preconditions.checkNotNull(result.getOtp(), "profileChangeEmailActionResult.otp is null");

        OtpActionResponse otpActionResponse = new OtpActionResponse()
                .setActionExpiredAt(result.getOtp().getExpiredAt().toString())
                .setOtpNumber(result.getOtp().getOtpNumber())
                .setOtpCountLeft(result.getOtp().getOtpCountLeft())
                .setOtpLastUpdate(result.getOtp().getOtpLastUpdate().toString())
                .setNewOtpDelay(propertyParseService.parseLong(otpNewDelaySeconds))
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());

        return new ProfileChangeEmailResponse()
                .setOtp(otpActionResponse)
                .setPwdInvalidAttemptsLeft(result.getPwdInvalidAttemptsLeft());
    }

    public ProfileChangePasswordActionRequest convertToProfileChangePasswordActionRequest(ProfileChangePasswordStartRequest request,
                                                                                          HttpServletRequest httpReqeust) {
        Preconditions.checkNotNull(request, "profileChangePasswordStartRequest is null!");
        Preconditions.checkNotNull(httpReqeust, "httpReqeust is null!");

        return new ProfileChangePasswordActionRequest()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setSessionId(SecurityUtils.getSession(httpReqeust).getId())
                .setNewPassword(request.getNewPassword())
                .setCurrentPassword(request.getCurrentPassword());
    }

    public ProfileChangePasswordActionRequest convertToProfileChangePasswordActionRequest(ProfileChangePasswordFinishRequest request,
                                                                                          HttpServletRequest httpRequest) {
        Preconditions.checkNotNull(request, "profileChangePasswordActionResult is null!");
        Preconditions.checkNotNull(httpRequest, "httpRequest is null!");

        return new ProfileChangePasswordActionRequest()
                .setSessionId(SecurityUtils.getSession(httpRequest).getId())
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setOtp(request.getOtp());
    }

    public ProfileChangePasswordResponse convertToProfileChangePasswordResponse(ProfileChangePasswordActionResult result) {
        Preconditions.checkNotNull(result, "profileChangePasswordActionResult is null");
        Preconditions.checkNotNull(result.getOtp(), "profileChangePasswordActionResult.otp is null");

        OtpActionResponse otpActionResponse = new OtpActionResponse()
                .setActionExpiredAt(result.getOtp().getExpiredAt().toString())
                .setOtpNumber(result.getOtp().getOtpNumber())
                .setOtpCountLeft(result.getOtp().getOtpCountLeft())
                .setOtpLastUpdate(result.getOtp().getOtpLastUpdate().toString())
                .setNewOtpDelay(propertyParseService.parseLong(otpNewDelaySeconds))
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());

        return new ProfileChangePasswordResponse()
                .setOtp(otpActionResponse)
                .setPwdInvalidAttemptsLeft(result.getPwdInvalidAttemptsLeft());
    }

    public UpdateSettings convertToUpdateSettings(UpdateSettingsRequest request) {
        Preconditions.checkNotNull(request, "updateSettingsRequest is null");
        return new UpdateSettings()
                .setEmail(SecurityUtils.getCurrentUser().getName())
                .setLanguage(request.getLanguage() != null ? Language.valueOf(request.getLanguage().toUpperCase()) : null)
                .setPrintMacronutrientsMode(request.getTelegramPrintMacronutrientsMode() != null ? PrintMacronutrientsMode.valueOf(request.getTelegramPrintMacronutrientsMode().toUpperCase()) : null)
                .setRoundAccuracy(request.getRoundAccuracy());
    }

    public SettingsResponse convertToSettingsResponse(ClientSettingsBean clientSettingsBean) {
        Preconditions.checkNotNull(clientSettingsBean, "clientSettingsBean is null");
        return new SettingsResponse()
                .setLanguage(clientSettingsBean.getLanguage().name())
                .setTelegramPrintMacronutrientsMode(clientSettingsBean.getPrintMacronutrientsMode().name())
                .setRoundAccuracy(clientSettingsBean.getRoundAccuracy());
    }

    private String resolveHeaderType(String filename, ConverterType converterType) {
        StringBuilder builder = new StringBuilder();
        builder.append(HEADER_PREFIX);

        if (StringUtils.isBlank(filename)) {
            builder
                    .append(DEFAULT_FILE_NAME);
        } else {
            builder
                    .append(filename);
        }
        builder.append(POINT);
        builder.append(converterType.getValue());
        return builder.toString();
    }

    private String resolveMediaType(ConverterType converterType) {
        switch (converterType) {
            case CSV:
                return "application/csv";
            case XLSX:
                return "application/xlsx";
            default:
                throw new IllegalArgumentException("Converter with type = [" + converterType + "] is not supported!");
        }
    }

    private Class resolveClass(ConverterType converterType) {
        switch (converterType) {
            case CSV:
                return DietRecordCsvRow.class;
            case XLSX:
                return DietRecordXlsxRow.class;
            default:
                throw new IllegalArgumentException("Converter with type = [" + converterType + "] is not supported!");
        }
    }
}
