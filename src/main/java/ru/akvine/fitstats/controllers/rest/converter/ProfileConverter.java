package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.converter.parser.Parser;
import ru.akvine.fitstats.controllers.rest.dto.profile.DisplayBiometricResponse;
import ru.akvine.fitstats.controllers.rest.dto.profile.ImportRecords;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricResponse;
import ru.akvine.fitstats.controllers.rest.dto.profile.delete.ProfileDeleteFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.delete.ProfileDeleteResponse;
import ru.akvine.fitstats.controllers.rest.dto.profile.file.DietRecordCsvRow;
import ru.akvine.fitstats.controllers.rest.dto.security.OtpActionResponse;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.services.dto.profile.delete.ProfileDeleteActionRequest;
import ru.akvine.fitstats.services.dto.profile.delete.ProfileDeleteActionResult;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class ProfileConverter {
    private static final String HEADER_PREFIX = "attachment; filename=";
    private static final String DEFAULT_FILE_NAME = "file";
    private static final String POINT = ".";

    private final Map<ConverterType, Parser> availableParsers;

    @Value("${security.otp.new.delay.seconds}")
    private int otpNewDelaySeconds;

    @Autowired
    public ProfileConverter(List<Parser> parsers) {
        this.availableParsers =
                parsers
                        .stream()
                        .collect(toMap(Parser::getType, identity()));
    }

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

    public ImportRecords convertToImportRecords(String converterType, MultipartFile file) {
        ConverterType type = ConverterType.valueOf(converterType);
        return new ImportRecords()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setRecords(availableParsers
                        .get(type)
                        .parse(file, resolveClass(type)));
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
                .setNewOtpDelay(otpNewDelaySeconds)
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());

        return new ProfileDeleteResponse()
                .setOtp(otpActionResponse)
                .setPwdInvalidAttemptsLeft(result.getPwdInvalidAttemptsLeft());
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

    private Class resolveClass(ConverterType converterType) {
        switch (converterType) {
            case CSV:
                return DietRecordCsvRow.class;
            default:
                throw new IllegalArgumentException("Converter with type = [" + converterType + "] is not supported!");
        }
    }
}
