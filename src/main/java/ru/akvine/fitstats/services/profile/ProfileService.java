package ru.akvine.fitstats.services.profile;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.controllers.rest.dto.profile.ImportRecords;
import ru.akvine.fitstats.controllers.rest.dto.profile.file.DietRecordCsvRow;
import ru.akvine.fitstats.entities.BiometricEntity;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.repositories.BiometricRepository;
import ru.akvine.fitstats.repositories.DietSettingRepository;
import ru.akvine.fitstats.services.*;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.diet.DietRecordBean;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.profile.DietRecordExport;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.services.processors.format.Converter;
import ru.akvine.fitstats.utils.DateUtils;

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
    private final ClientService clientService;
    private final ProductService productService;

    @Autowired
    public ProfileService(List<Converter> converters,
                          DietService dietService,
                          BiometricService biometricService,
                          BiometricRepository biometricRepository,
                          DietSettingService dietSettingService,
                          DietSettingRepository dietSettingRepository,
                          ClientService clientService,
                          ProductService productService) {
        this.dietService = dietService;
        this.dietSettingService = dietSettingService;
        this.biometricRepository = biometricRepository;
        this.biometricService = biometricService;
        this.dietSettingRepository = dietSettingRepository;
        this.clientService = clientService;
        this.productService = productService;
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

    public void importRecords(ImportRecords importRecords) {
        Preconditions.checkNotNull(importRecords, "importRecords is null");

        String clientUuid = importRecords.getClientUuid();
        List<?> records = importRecords.getRecords();

        // TODO : получение списка продуктов через цикл - может нагружать БД

        ClientBean clientBean = clientService.getByUuid(clientUuid);
        records.forEach(record -> {
            if (record instanceof DietRecordCsvRow) {
                DietRecordCsvRow csvRow = (DietRecordCsvRow) record;
                ProductBean productBean = new ProductBean(productService.findByUuid(csvRow.getUuid()));
                DietRecordBean dietRecordBean = new DietRecordBean()
                        .setClientBean(clientBean)
                        .setProductBean(productBean)
                        .setProteins(Double.parseDouble(csvRow.getProteins()))
                        .setFats(Double.parseDouble(csvRow.getFats()))
                        .setCarbohydrates(Double.parseDouble(csvRow.getCarbohydrates()))
                        .setVol(Double.parseDouble(csvRow.getVol()))
                        .setCalories(Double.parseDouble(csvRow.getCalories()))
                        .setVolume(Double.parseDouble(csvRow.getVolume()))
                        .setDate(DateUtils.convertToLocalDate(csvRow.getDate()))
                        .setTime(StringUtils.isBlank(csvRow.getTime()) ? null : DateUtils.convertToLocalTime(csvRow.getTime()));
                dietService.add(dietRecordBean);
            }
        });
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
        BiometricBean savedBiometricBean = new BiometricBean(biometricRepository.save(biometricEntity));

        if (updateBiometric.isUpdateDietSetting()) {
            DietSettingEntity dietSettingEntity = dietSettingService.verifyExistsAndGet(clientUuid);
            Macronutrients macronutrients = dietService.calculate(savedBiometricBean, dietSettingEntity.getDiet());

            dietSettingEntity.setMaxProteins(macronutrients.getProteins());
            dietSettingEntity.setMaxFats(macronutrients.getFats());
            dietSettingEntity.setMaxCarbohydrates(macronutrients.getCarbohydrates());
            dietSettingEntity.setMaxCalories(macronutrients.getCalories());
            dietSettingEntity.setUpdatedDate(LocalDateTime.now());
            dietSettingRepository.save(dietSettingEntity);
        }

        return savedBiometricBean;
    }

    public BiometricBean display(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        return new BiometricBean(biometricService.verifyExistsAndGet(clientUuid));
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
