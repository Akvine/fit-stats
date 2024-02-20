package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.converter.ProfileConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.profile.ImportRecords;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateSettingsRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_email.ProfileChangeEmailFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_email.ProfileChangeEmailStartRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_password.ProfileChangePasswordFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_password.ProfileChangePasswordStartRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.delete.ProfileDeleteFinishRequest;
import ru.akvine.fitstats.controllers.rest.meta.profile.ProfileChangeEmailControllerMeta;
import ru.akvine.fitstats.controllers.rest.meta.profile.ProfileChangePasswordControllerMeta;
import ru.akvine.fitstats.controllers.rest.meta.profile.ProfileControllerMeta;
import ru.akvine.fitstats.controllers.rest.meta.profile.ProfileDeleteControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.ProfileValidator;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.services.dto.profile.UpdateSettings;
import ru.akvine.fitstats.services.dto.profile.change_email.ProfileChangeEmailActionRequest;
import ru.akvine.fitstats.services.dto.profile.change_email.ProfileChangeEmailActionResult;
import ru.akvine.fitstats.services.dto.profile.change_password.ProfileChangePasswordActionRequest;
import ru.akvine.fitstats.services.dto.profile.change_password.ProfileChangePasswordActionResult;
import ru.akvine.fitstats.services.dto.profile.delete.ProfileDeleteActionRequest;
import ru.akvine.fitstats.services.dto.profile.delete.ProfileDeleteActionResult;
import ru.akvine.fitstats.services.profile.ProfileChangeEmailActionService;
import ru.akvine.fitstats.services.profile.ProfileChangePasswordActionService;
import ru.akvine.fitstats.services.profile.ProfileDeleteActionService;
import ru.akvine.fitstats.services.profile.ProfileService;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController implements ProfileControllerMeta,
        ProfileDeleteControllerMeta, ProfileChangeEmailControllerMeta, ProfileChangePasswordControllerMeta {
    private final ProfileConverter profileConverter;
    private final ProfileValidator profileValidator;
    private final ProfileService profileService;

    private final ProfileDeleteActionService profileDeleteActionService;
    private final ProfileChangeEmailActionService profileChangeEmailActionService;
    private final ProfileChangePasswordActionService profileChangePasswordActionService;

    @Override
    public ResponseEntity exportRecords(LocalDate startDate,
                                        LocalDate endDate,
                                        String duration,
                                        String filename,
                                        String converterType) {
        profileValidator.verifyRecordsExport(
                startDate,
                endDate,
                duration,
                converterType);
        ProfileDownload profileDownload = profileConverter.convertToProfileDownload(startDate, endDate, duration, converterType);
        byte[] file = profileService.exportRecords(profileDownload);
        return profileConverter.convertToExportResponse(file, filename, profileDownload.getConverterType());
    }

    @Override
    public Response importRecords(String converterType, MultipartFile file) {
        profileValidator.verifyRecordsImport(converterType, file);
        ImportRecords importRecords = profileConverter.convertToImportRecords(converterType, file);
        profileValidator.verifyRecordsCount(importRecords);
        profileService.importRecords(importRecords);
        return new SuccessfulResponse();
    }

    @Override
    public Response updateBiometric(@Valid UpdateBiometricRequest request) {
        profileValidator.verifyUpdateBiometricRequest(request);
        UpdateBiometric updateBiometric = profileConverter.convertToUpdateBiometric(request);
        BiometricBean biometricBean = profileService.updateBiometric(updateBiometric);
        return profileConverter.convertToUpdateBiometricResponse(biometricBean);
    }

    @Override
    public Response displayBiometric() {
        String clientUuid = SecurityUtils.getCurrentUser().getUuid();
        BiometricBean biometricBean = profileService.display(clientUuid);
        return profileConverter.convertToDisplayBiometricResponse(biometricBean);
    }

    @Override
    public Response listSettings() {
        ClientSettingsBean clientSettingsBean = profileService.listSettings(SecurityUtils.getCurrentUser().getName());
        return profileConverter.convertToSettingsResponse(clientSettingsBean);
    }

    @Override
    public Response updateSettings(@Valid UpdateSettingsRequest request) {
        profileValidator.verifyUpdateSettingsRequest(request);
        UpdateSettings updateSettings = profileConverter.convertToUpdateSettings(request);
        ClientSettingsBean clientSettingsBean = profileService.updateSettings(updateSettings);
        return profileConverter.convertToSettingsResponse(clientSettingsBean);
    }

    @Override
    public Response deleteStart(HttpServletRequest httpServletRequest) {
        ProfileDeleteActionRequest request = profileConverter.convertToProfileDeleteActionRequest(httpServletRequest);
        ProfileDeleteActionResult result = profileDeleteActionService.startDelete(request);
        return profileConverter.convertToProfileDeleteResponse(result);
    }

    @Override
    public Response deleteNewOtp() {
        String clientUuid = SecurityUtils.getCurrentUser().getUuid();
        ProfileDeleteActionResult result = profileDeleteActionService.newOtpProfileDelete(clientUuid);
        return profileConverter.convertToProfileDeleteResponse(result);
    }

    @Override
    public Response deleteFinish(@Valid @RequestBody ProfileDeleteFinishRequest request, HttpServletRequest httpServletRequest) {
        ProfileDeleteActionRequest deleteActionRequest = profileConverter.convertToProfileDeleteActionRequest(request, httpServletRequest);
        profileDeleteActionService.finishDelete(deleteActionRequest);
        SecurityUtils.doLogout(httpServletRequest);
        return new SuccessfulResponse();
    }

    @Override
    public Response changeEmailStart(@Valid @RequestBody ProfileChangeEmailStartRequest request, HttpServletRequest httpServletRequest) {
        profileValidator.verifyProfileChangeEmailStartRequest(request);
        ProfileChangeEmailActionRequest start = profileConverter.convertToProfileChangeEmailActionRequest(request, httpServletRequest);
        ProfileChangeEmailActionResult result = profileChangeEmailActionService.startChangeEmail(start);
        return profileConverter.convertToProfileChangeEmailResponse(result);
    }

    @Override
    public Response changeEmailNewOtp() {
        String clientUuid = SecurityUtils.getCurrentUser().getUuid();
        ProfileChangeEmailActionResult result = profileChangeEmailActionService.newOtpEmailChange(clientUuid);
        return profileConverter.convertToProfileChangeEmailResponse(result);
    }

    @Override
    public Response changeEmailFinish(@Valid ProfileChangeEmailFinishRequest request, HttpServletRequest httpServletRequest) {
        ProfileChangeEmailActionRequest profileChangeEmailActionRequest = profileConverter.convertToProfileChangeEmailActionRequest(request, httpServletRequest);
        profileChangeEmailActionService.finishEmailChange(profileChangeEmailActionRequest);
        return new SuccessfulResponse();
    }

    @Override
    public Response changePasswordStart(@Valid ProfileChangePasswordStartRequest request, HttpServletRequest httpServletRequest) {
        profileValidator.verifyProfileChangePasswordStartRequest(request);
        ProfileChangePasswordActionRequest start = profileConverter.convertToProfileChangePasswordActionRequest(request, httpServletRequest);
        ProfileChangePasswordActionResult result = profileChangePasswordActionService.startChangePassword(start);
        return profileConverter.convertToProfileChangePasswordResponse(result);
    }

    @Override
    public Response changePasswordNewOtp() {
        String clientUuid = SecurityUtils.getCurrentUser().getUuid();
        ProfileChangePasswordActionResult result = profileChangePasswordActionService.newOtpChangePassword(clientUuid);
        return profileConverter.convertToProfileChangePasswordResponse(result);
    }

    @Override
    public Response changePasswordFinish(@Valid ProfileChangePasswordFinishRequest request, HttpServletRequest httpServletRequest) {
        ProfileChangePasswordActionRequest start = profileConverter.convertToProfileChangePasswordActionRequest(request, httpServletRequest);
        profileChangePasswordActionService.finishChangePassword(start);
        return new SuccessfulResponse();
    }
}
