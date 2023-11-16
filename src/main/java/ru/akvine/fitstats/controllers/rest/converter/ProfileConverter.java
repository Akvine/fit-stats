package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricResponse;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.time.LocalDate;

@Component
public class ProfileConverter {
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
}
