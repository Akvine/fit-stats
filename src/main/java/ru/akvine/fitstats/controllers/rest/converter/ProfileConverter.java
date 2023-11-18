package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricResponse;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.time.LocalDate;

@Component
public class ProfileConverter {
    private static final String HEADER_PREFIX = "attachment; filename=";
    private static final String DEFAULT_FILE_NAME = "file";
    private static final String POINT = ".";

    public ProfileDownload convertToProfileDownload(
            LocalDate startDate,
            LocalDate endDate,
            String duration,
            String converterType) {
        return new ProfileDownload()
                .setConverterType(converterType == null ? ConverterType.CSV : ConverterType.valueOf(converterType))
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

    public UpdateBiometric convertToUpdateBiometric(UpdateBiometricRequest request) {
        Preconditions.checkNotNull(request, "updateBiometricRequest is null");
        return new UpdateBiometric()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
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
            default:
                throw new IllegalArgumentException("Converter with type = [" + converterType + "] is not supported!");
        }
    }
}
