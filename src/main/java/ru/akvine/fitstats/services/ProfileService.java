package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.BiometricEntity;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.repositories.BiometricRepository;
import ru.akvine.fitstats.repositories.DietSettingRepository;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.profile.DietRecordExport;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.services.processors.format.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static ru.akvine.fitstats.utils.DateUtils.*;

@Service
@Slf4j
public class ProfileService {
    private final Map<ConverterType, Converter> availableConverters;
    private final DietService dietService;
    private final BiometricService biometricService;
    private final BiometricRepository biometricRepository;
    private final DietSettingService dietSettingService;
    private final DietSettingRepository dietSettingRepository;

    @Autowired
    public ProfileService(List<Converter> converters,
                          DietService dietService,
                          BiometricService biometricService,
                          BiometricRepository biometricRepository,
                          DietSettingService dietSettingService,
                          DietSettingRepository dietSettingRepository) {
        this.dietService = dietService;
        this.dietSettingService = dietSettingService;
        this.biometricRepository = biometricRepository;
        this.biometricService = biometricService;
        this.dietSettingRepository = dietSettingRepository;
        this.availableConverters = converters
                .stream()
                .collect(toMap(Converter::getType, identity()));
    }

    public byte[] exportRecords(ProfileDownload profileDownload) {
        Preconditions.checkNotNull(profileDownload, "profileDownload is null");

        DateRange getDateRange = getDateRange(profileDownload);
        List<DietRecordExport> dietRecordsExport = dietService.findByDateRange(
                profileDownload.getClientUuid(),
                getDateRange.getStartDate(),
                getDateRange.getEndDate());
        return availableConverters
                .get(profileDownload.getConverterType())
                .convert(dietRecordsExport, DietRecordExport.class);
    }

    public BiometricBean updateBiometric(UpdateBiometric updateBiometric) {
        Preconditions.checkNotNull(updateBiometric, "updateBiometric is null");

        String clientUuid = updateBiometric.getClientUuid();
        BiometricEntity biometricEntity = biometricService.verifyExistsAndGet(clientUuid);
        if (updateBiometric.getAge() != null) {
            biometricEntity.setAge(updateBiometric.getAge());
        }
        if (StringUtils.isNotBlank(updateBiometric.getHeight())) {
            biometricEntity.setHeight(updateBiometric.getHeight());
        }
        if (StringUtils.isNotBlank(updateBiometric.getWeight())) {
            biometricEntity.setWeight(updateBiometric.getWeight());
        }
        if (updateBiometric.getPhysicalActivity() != null) {
            biometricEntity.setPhysicalActivity(updateBiometric.getPhysicalActivity());
        }
        biometricEntity.setUpdatedDate(LocalDateTime.now());
        DietSettingEntity dietSettingEntity = dietSettingService.verifyExistsAndGet(clientUuid);
        BiometricBean savedBiometricBean = new BiometricBean(biometricRepository.save(biometricEntity));
        Macronutrients macronutrients = dietService.calculate(savedBiometricBean, dietSettingEntity.getDiet());

        dietSettingEntity.setMaxProteins(macronutrients.getProteins());
        dietSettingEntity.setMaxFats(macronutrients.getFats());
        dietSettingEntity.setMaxCarbohydrates(macronutrients.getCarbohydrates());
        dietSettingEntity.setMaxCalories(macronutrients.getCalories());
        dietSettingEntity.setUpdatedDate(LocalDateTime.now());
        dietSettingRepository.save(dietSettingEntity);

        return savedBiometricBean;
    }

    private DateRange getDateRange(ProfileDownload profileDownload) {
        LocalDate startDate = profileDownload.getStartDate();
        LocalDate endDate = profileDownload.getEndDate();
        Duration duration = profileDownload.getDuration();

        DateRange findDateRange;

        if (profileDownload.getDuration() != null) {
            switch (duration) {
                case DAY:
                    findDateRange = getDayRange();
                    break;
                case WEEK:
                    findDateRange = getWeekRange();
                    break;
                case MONTH:
                    findDateRange = getMonthRange();
                    break;
                default:
                    findDateRange = getYearRange();
            }
        } else {
            findDateRange = new DateRange(startDate, endDate);
        }
        return findDateRange;
    }
}
