package ru.akvine.fitstats.services.profile;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.controllers.rest.dto.profile.ImportRecords;
import ru.akvine.fitstats.controllers.rest.dto.profile.file.DietRecordCsvRow;
import ru.akvine.fitstats.controllers.rest.dto.profile.file.DietRecordXlsxRow;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.services.BiometricService;
import ru.akvine.fitstats.services.ClientService;
import ru.akvine.fitstats.services.DietService;
import ru.akvine.fitstats.services.ProductService;
import ru.akvine.fitstats.services.dto.DateRange;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static ru.akvine.fitstats.utils.DateUtils.*;

@Service
@Slf4j
public class ProfileService {
    private final Map<ConverterType, Converter> availableConverters;
    private final DietService dietService;
    private final BiometricService biometricService;
    private final ClientService clientService;
    private final ProductService productService;

    @Autowired
    public ProfileService(List<Converter> converters,
                          DietService dietService,
                          BiometricService biometricService,
                          ClientService clientService,
                          ProductService productService) {
        this.dietService = dietService;
        this.biometricService = biometricService;
        this.clientService = clientService;
        this.productService = productService;
        this.availableConverters = converters
                .stream()
                .collect(toMap(Converter::getType, identity()));
    }

    public byte[] exportRecords(ProfileDownload profileDownload) {
        Preconditions.checkNotNull(profileDownload, "profileDownload is null");
        logger.info("Export records for client with uuid = {}", profileDownload.getClientUuid());

        DateRange getDateRange = getDateRange(profileDownload);
        List<DietRecordExport> dietRecordsExport = dietService.findByDateRange(
                profileDownload.getClientUuid(),
                getDateRange.getStartDate(),
                getDateRange.getEndDate());
        byte[] records = availableConverters
                .get(profileDownload.getConverterType())
                .convert(dietRecordsExport, DietRecordExport.class);
        logger.info("Successful export records for client with uuid = {}", profileDownload.getClientUuid());
        return records;
    }

    public void importRecords(ImportRecords importRecords) {
        Preconditions.checkNotNull(importRecords, "importRecords is null");

        String clientUuid = importRecords.getClientUuid();
        List<?> records = importRecords.getRecords();
        logger.info("Import records for client with uuid = {}", importRecords.getClientUuid());

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
                        .setAlcohol(Double.parseDouble(csvRow.getAlcohol()))
                        .setVol(Double.parseDouble(csvRow.getVol()))
                        .setCalories(Double.parseDouble(csvRow.getCalories()))
                        .setVolume(Double.parseDouble(csvRow.getVolume()))
                        .setDate(DateUtils.convertToLocalDate(csvRow.getDate()))
                        .setTime(StringUtils.isBlank(csvRow.getTime()) ? null : DateUtils.convertToLocalTime(csvRow.getTime()));
                dietService.add(dietRecordBean);
            }
            if (record instanceof DietRecordXlsxRow) {
                DietRecordXlsxRow xlsxRow = (DietRecordXlsxRow) record;
                ProductBean productBean = new ProductBean(productService.findByUuid(xlsxRow.getUuid()));
                DietRecordBean dietRecordBean = new DietRecordBean()
                        .setClientBean(clientBean)
                        .setProductBean(productBean)
                        .setProteins(Double.parseDouble(xlsxRow.getProteins()))
                        .setFats(Double.parseDouble(xlsxRow.getFats()))
                        .setCarbohydrates(Double.parseDouble(xlsxRow.getCarbohydrates()))
                        .setAlcohol(Double.parseDouble(xlsxRow.getAlcohol()))
                        .setVol(Double.parseDouble(xlsxRow.getVol()))
                        .setCalories(Double.parseDouble(xlsxRow.getCalories()))
                        .setVolume(Double.parseDouble(xlsxRow.getVolume()))
                        .setDate(DateUtils.convertToLocalDate(xlsxRow.getDate()))
                        .setTime(StringUtils.isBlank(xlsxRow.getTime()) ? null : DateUtils.convertToLocalTime(xlsxRow.getTime()));
                dietService.add(dietRecordBean);
            }
        });
        logger.info("Successful import records for client with uuid = {}", clientUuid);
    }

    public BiometricBean updateBiometric(UpdateBiometric updateBiometric) {
       return biometricService.update(updateBiometric);
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
            switch (Objects.requireNonNull(duration)) {
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
