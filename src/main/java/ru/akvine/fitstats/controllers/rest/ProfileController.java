package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.converter.ProfileConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.profile.ImportRecords;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricRequest;
import ru.akvine.fitstats.controllers.rest.meta.ProfileControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.ProfileValidator;
import ru.akvine.fitstats.services.ProfileService;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController implements ProfileControllerMeta {
    private final ProfileConverter profileConverter;
    private final ProfileValidator profileValidator;
    private final ProfileService profileService;

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
}
