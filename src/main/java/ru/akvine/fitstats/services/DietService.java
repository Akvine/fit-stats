package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.exceptions.diet.DietRecordNotFoundException;
import ru.akvine.fitstats.exceptions.diet.DietRecordsNotUniqueResultException;
import ru.akvine.fitstats.exceptions.diet.ProductsNotUniqueResultException;
import ru.akvine.fitstats.repositories.DietRecordRepository;
import ru.akvine.fitstats.services.client.ClientService;
import ru.akvine.fitstats.services.dto.DateRange;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.diet.*;
import ru.akvine.fitstats.services.dto.product.GetByBarCode;
import ru.akvine.fitstats.services.dto.profile.DietRecordExport;
import ru.akvine.fitstats.services.listeners.AddDietRecordEvent;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.utils.UUIDGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.akvine.fitstats.utils.DateUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietService {
    private final DietRecordRepository dietRecordRepository;
    private final DietSettingService dietSettingService;
    private final ClientService clientService;
    private final ProductService productService;
    private final BiometricService biometricService;
    private final MacronutrientsCalculationService macronutrientsCalculationService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PropertyParseService propertyParseService;

    private final static int SINGLE_ELEMENT = 0;

    @Value("uuid.length")
    private String length;

    @Transactional
    public AddDietRecordFinish add(AddDietRecordStart addDietRecordStart) {
        Preconditions.checkNotNull(addDietRecordStart, "addDietRecordStart is null");
        logger.info("Try to add diet record = [{}]", addDietRecordStart);

        String clientUuid = addDietRecordStart.getClientUuid();
        ClientEntity clientEntity = clientService.verifyExistsByUuidAndGet(clientUuid);
        String productUuid = addDietRecordStart.getProductUuid();
        ProductEntity productEntity;
        if (productUuid.length() < propertyParseService.parseInteger(length)) {
            List<ProductEntity> products = productService.verifyExistsByPartialUuidAndGet(productUuid);
            if (products.size() != 1) {
                throw new ProductsNotUniqueResultException("Products by uuid = [" + productUuid + "] is not contains single element!");
            }
            productEntity = products.get(SINGLE_ELEMENT);
        } else {
            productEntity = productService.verifyExistsAndGet(productUuid);
        }

        Macronutrients consumedMacronutrients = macronutrientsCalculationService.calculate(
                productEntity.getProteins(),
                productEntity.getFats(),
                productEntity.getCarbohydrates(),
                productEntity.getAlcohol(),
                addDietRecordStart.getVolume());

        DietRecordEntity dietRecordEntity = new DietRecordEntity()
                .setUuid(UUIDGenerator.uuidWithoutDashes(propertyParseService.parseInteger(length)))
                .setProteins(consumedMacronutrients.getProteins())
                .setFats(consumedMacronutrients.getFats())
                .setCarbohydrates(consumedMacronutrients.getCarbohydrates())
                .setCalories(consumedMacronutrients.getCalories())
                .setAlcohol(consumedMacronutrients.getAlcohol())
                .setVol(productEntity.getVol())
                .setVolume(addDietRecordStart.getVolume())
                .setDate(addDietRecordStart.getDate())
                .setTime(addDietRecordStart.getTime())
                .setClient(clientEntity)
                .setProduct(productEntity);

        DietRecordBean dietRecordBean = new DietRecordBean(dietRecordRepository.save(dietRecordEntity));

        Long clientId = clientEntity.getId();
        DietSettingEntity dietSettingEntity = dietSettingService.verifyExistsAndGet(clientUuid);
        List<DietRecordEntity> records = dietRecordRepository.findByDate(clientUuid, LocalDate.now());
        applicationEventPublisher.publishEvent(new AddDietRecordEvent(new Object(), clientId, dietSettingEntity, records));

        logger.info("Successful add diet record = [{}]", dietRecordBean);
        return new AddDietRecordFinish()
                .setUuid(dietRecordBean.getUuid())
                .setProductTitle(productEntity.getTitle())
                .setProductUuid(productUuid)
                .setProteins(dietRecordBean.getProteins())
                .setFats(dietRecordBean.getFats())
                .setCarbohydrates(dietRecordBean.getCarbohydrates())
                .setCalories(dietRecordBean.getCalories())
                .setAlcohol(dietRecordBean.getAlcohol())
                .setVol(dietRecordBean.getVol())
                .setVolume(dietRecordBean.getVolume())
                .setVolumeMeasurement(dietRecordBean.getProductBean().getMeasurement());
    }

    public AddDietRecordFinish addByBarCode(AddDietRecordStart addDietRecordStart) {
        Preconditions.checkNotNull(addDietRecordStart, "addDietRecordStart is null");
        GetByBarCode getByBarCode = new GetByBarCode()
                .setClientUuid(addDietRecordStart.getClientUuid())
                .setNumber(addDietRecordStart.getBarCodeNumber());
        String productUuid = productService.getByBarCodeNumber(getByBarCode).getUuid();
        addDietRecordStart.setProductUuid(productUuid);
        return add(addDietRecordStart);
    }

    public DietRecordBean add(DietRecordBean dietRecordBean) {
        Preconditions.checkNotNull(dietRecordBean, "dietRecordBean is null");
        logger.info("Try to add diet record = [{}]", dietRecordBean);

        String clientUuid = dietRecordBean.getClientBean().getUuid();
        ClientEntity clientEntity = clientService.verifyExistsByUuidAndGet(clientUuid);
        String productUuid = dietRecordBean.getProductBean().getUuid();
        ProductEntity productEntity = productService.findByUuid(productUuid);

        DietRecordEntity dietRecordEntity = new DietRecordEntity()
                .setUuid(UUIDGenerator.uuidWithoutDashes(propertyParseService.parseInteger(length)))
                .setProteins(dietRecordBean.getProteins())
                .setFats(dietRecordBean.getFats())
                .setCarbohydrates(dietRecordBean.getCarbohydrates())
                .setAlcohol(dietRecordBean.getAlcohol())
                .setCalories(dietRecordBean.getCalories())
                .setVolume(dietRecordBean.getVolume())
                .setVol(dietRecordBean.getVol())
                .setDate(dietRecordBean.getDate())
                .setTime(dietRecordBean.getTime())
                .setClient(clientEntity)
                .setProduct(productEntity);

        DietRecordBean savedDietRecordBean = new DietRecordBean(dietRecordRepository.save(dietRecordEntity));
        logger.info("Successful add diet record = [{}]", savedDietRecordBean);
        return savedDietRecordBean;
    }

    @Transactional
    public ListRecordsFinish list(ListRecordsStart listRecordsStart) {
        Preconditions.checkNotNull(listRecordsStart, "listRecord is null");
        logger.info("Try to get list diet records = [{}]", listRecordsStart);

        String clientUuid = listRecordsStart.getClientUuid();
        clientService.verifyExistsByUuidAndGet(clientUuid);

        LocalDate date;
        if (listRecordsStart.getDate() == null) {
            date = LocalDate.now();
        } else {
            date = listRecordsStart.getDate();
        }

        List<DietRecordBean> records = dietRecordRepository
                .findByDate(clientUuid, date)
                .stream()
                .map(DietRecordBean::new)
                .collect(Collectors.toList());
        return new ListRecordsFinish()
                .setRecords(records);
    }

    public void deleteRecord(DeleteRecord deleteRecord) {
        Preconditions.checkNotNull(deleteRecord, "deleteRecords is null");
        logger.info("Try to delete record = [{}]", deleteRecord);

        String clientUuid = deleteRecord.getClientUuid();
        clientService.verifyExistsByUuidAndGet(clientUuid);

        String dietRecordUuid = deleteRecord.getRecordUuid();
        DateRange dateRange = deleteRecord.getDateRange();
        if (StringUtils.isNotBlank(dietRecordUuid)) {
            deleteRecordByUuid(dietRecordUuid, clientUuid);
        } else {
            deleteRecordsByDateRange(dateRange, clientUuid);
        }
    }

    public DietDisplay display(Display display) {
        Preconditions.checkNotNull(display, "display is null");
        logger.info("Display diet macronutrients = [{}]", display);

        String clientUuid = display.getClientUuid();
        clientService.verifyExistsByUuidAndGet(clientUuid);

        LocalDate date;
        if (display.getDate() == null) {
            date = LocalDate.now();
        } else {
            date = display.getDate();
        }

        List<DietRecordEntity> records = dietRecordRepository.findByDate(clientUuid, date);
        DietSettingEntity dietSettingEntity = dietSettingService.verifyExistsAndGet(clientUuid);

        double maxCalories = dietSettingEntity.getMaxCalories();
        double maxProteins = dietSettingEntity.getMaxProteins();
        double maxFats = dietSettingEntity.getMaxFats();
        double maxCarbohydrates = dietSettingEntity.getMaxCarbohydrates();
        double currentCalories = records.stream().mapToDouble(DietRecordEntity::getCalories).sum();
        double currentProteins = records.stream().mapToDouble(DietRecordEntity::getProteins).sum();
        double currentFats = records.stream().mapToDouble(DietRecordEntity::getFats).sum();
        double currentCarbohydrates = records.stream().mapToDouble(DietRecordEntity::getCarbohydrates).sum();
        double remainingProteins = maxProteins - currentProteins;
        double remainingFats = maxFats - currentFats;
        double remainingCarbohydrates = maxCarbohydrates - currentCarbohydrates;
        double remainingCalories = maxCalories - currentCalories;

        return new DietDisplay()
                .setMaxCalories(maxCalories)
                .setMaxProteins(maxProteins)
                .setMaxFats(maxFats)
                .setMaxCarbohydrates(maxCarbohydrates)
                .setCurrentCalories(currentCalories)
                .setCurrentProteins(currentProteins)
                .setCurrentFats(currentFats)
                .setCurrentCarbohydrates(currentCarbohydrates)
                .setRemainingCalories(remainingCalories)
                .setRemainingProteins(remainingProteins)
                .setRemainingFats(remainingFats)
                .setRemainingCarbohydrates(remainingCarbohydrates);
    }

    public void changeDiet(ChangeDiet changeDiet) {
        BiometricBean biometricBean = biometricService.getByClientUuid(changeDiet.getClientUuid());
        dietSettingService.changeDiet(biometricBean, changeDiet);
    }

    public List<DietRecordExport> findByDateRange(String clientUuid,
                                                  LocalDate startDate,
                                                  LocalDate endDate) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        Preconditions.checkNotNull(startDate, "startDate is null");
        Preconditions.checkNotNull(endDate, "endDate is null");
        return dietRecordRepository
                .findByDateRange(clientUuid, startDate, endDate)
                .stream()
                .map(DietRecordBean::new)
                .map(DietRecordExport::new)
                .collect(Collectors.toList());
    }

    public DietRecordEntity verifyExistsByUuidAndGet(String uuid, String clientUuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        return dietRecordRepository
                .findByUuid(uuid, clientUuid)
                .orElseThrow(() -> new DietRecordNotFoundException("Diet with uuid = [" + uuid + "] not found!"));
    }

    public List<DietRecordEntity> verifyExistsByPartialUuidAndGet(String uuid, String clientUuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        return dietRecordRepository.findByPartialUuid(uuid, clientUuid);
    }

    private void deleteRecordByUuid(String dietRecordUuid, String clientUuid) {
        DietRecordEntity dietRecordEntity;
        if (dietRecordUuid.length() < propertyParseService.parseInteger(length)) {
            List<DietRecordEntity> records = verifyExistsByPartialUuidAndGet(dietRecordUuid, clientUuid);
            if (records.size() != 1) {
                throw new DietRecordsNotUniqueResultException("Diet records by uuid = [" + dietRecordUuid + "] is not contains single element!");
            }
            dietRecordEntity = records.get(SINGLE_ELEMENT);
        } else {
            dietRecordEntity = verifyExistsByUuidAndGet(dietRecordUuid, clientUuid);
        }


        dietRecordRepository.delete(dietRecordEntity);
        logger.info("Successful delete record with uuid = {} for client with uuid = {}", dietRecordUuid, clientUuid);
    }

    private void deleteRecordsByDateRange(DateRange dateRange, String clientUuid) {
        DateRange findDateRange;
        if (dateRange.getDuration() != null) {
            switch (Objects.requireNonNull(dateRange.getDuration())) {
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
            findDateRange = new DateRange(dateRange.getStartDate(), dateRange.getEndDate());
        }

        List<DietRecordEntity> records = dietRecordRepository.findByDateRange(
                clientUuid,
                findDateRange.getStartDate(),
                findDateRange.getEndDate());
        dietRecordRepository.deleteAll(records);
        logger.info("Successful delete records for client with uuid = {}", clientUuid);
    }
}
