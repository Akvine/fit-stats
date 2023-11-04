package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.enums.*;
import ru.akvine.fitstats.exceptions.client.ClientNotFoundException;
import ru.akvine.fitstats.repositories.ClientRepository;
import ru.akvine.fitstats.repositories.DietRecordRepository;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.diet.*;
import ru.akvine.fitstats.services.dto.profile.DietRecordExport;
import ru.akvine.fitstats.utils.DietUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.akvine.fitstats.utils.DietUtils.calculateMacronutrients;
import static ru.akvine.fitstats.utils.DietUtils.transformPer100;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietService {
    private final DietRecordRepository dietRecordRepository;
    private final DietSettingService dietSettingService;
    private final ClientRepository clientRepository;
    private final ProductService productService;

    private final static double GAIN_PROTEIN_COEFFICIENT = 1.7;
    private final static double GAIN_FATS_COEFFICIENT = 1.2;
    private final static double DRYING_FATS_COEFFICIENT = 0.7;

    public Macronutrients calculate(BiometricBean biometricBean, Diet diet) {
        Preconditions.checkNotNull(biometricBean, "biometricBean is null");
        Preconditions.checkNotNull(diet, "diet is null");

        HeightMeasurement heightMeasurement = biometricBean.getHeightMeasurement();
        WeightMeasurement weightMeasurement = biometricBean.getWeightMeasurement();
        Gender gender = biometricBean.getGender();
        PhysicalActivity physicalActivity = biometricBean.getPhysicalActivity();
        int age = biometricBean.getAge();
        double height = DietUtils.convertToCm(Double.parseDouble(biometricBean.getHeight()), heightMeasurement);
        double weight = DietUtils.convertToKg(Double.parseDouble(biometricBean.getWeight()), weightMeasurement);

        double basicExchange = DietUtils.calculateBasicExchange(gender, age, height, weight);
        double dailyCaloriesIntake = DietUtils.calculateDailyCaloriesIntake(basicExchange, physicalActivity);

        double maxCalories = dailyCaloriesIntake;
        double maxProteins;
        double maxFats;
        double maxCarbohydrates;

        switch (diet) {
            case GAIN:
                maxCalories = dailyCaloriesIntake + dailyCaloriesIntake * 0.15;
                maxProteins = weight * GAIN_PROTEIN_COEFFICIENT;
                maxFats = weight * GAIN_FATS_COEFFICIENT;
                break;
            case RETENTION:
                maxProteins = weight;
                maxFats = weight;
                break;
            case DRYING:
                maxCalories = dailyCaloriesIntake - dailyCaloriesIntake * 0.2;
                maxProteins = gender == Gender.MALE ? (height - 100) * GAIN_PROTEIN_COEFFICIENT
                        : (height - 110) * GAIN_PROTEIN_COEFFICIENT;
                maxFats = gender == Gender.MALE ? (height - 100) * DRYING_FATS_COEFFICIENT : (height - 110) * DRYING_FATS_COEFFICIENT;
                break;
            default:
                throw new IllegalStateException("Diet type = [" + diet + "] is not supported!");
        }
        maxCarbohydrates = (maxCalories - (maxProteins * 4 + maxFats * 9)) / 4;
        return new Macronutrients(maxProteins, maxFats, maxCarbohydrates, maxCalories);
    }

    public AddDietRecordFinish add(AddDietRecordStart addDietRecordStart) {
        Preconditions.checkNotNull(addDietRecordStart, "addDietRecordStart is null");

        String clientUuid = addDietRecordStart.getClientUuid();
        ClientEntity clientEntity = clientRepository
                .findByUuid(clientUuid)
                .orElseThrow(() -> new ClientNotFoundException("Client with uuid = [" + clientUuid + "] not found!"));
        String productUuid = addDietRecordStart.getProductUuid();
        ProductEntity productEntity = productService.verifyExistsAndGet(productUuid);

        Macronutrients macronutrientsPer100 = transformPer100(
                productEntity.getProteins(),
                productEntity.getFats(),
                productEntity.getCarbohydrates(),
                productEntity.getVolume());
        Macronutrients consumedMacronutrients = calculateMacronutrients(
                macronutrientsPer100.getProteins(),
                macronutrientsPer100.getFats(),
                macronutrientsPer100.getCarbohydrates(),
                addDietRecordStart.getVolume());

        DietRecordEntity dietRecordEntity = new DietRecordEntity()
                .setProteins(consumedMacronutrients.getProteins())
                .setFats(consumedMacronutrients.getFats())
                .setCarbohydrates(consumedMacronutrients.getCarbohydrates())
                .setCalories(consumedMacronutrients.getCalories())
                .setVolume(addDietRecordStart.getVolume())
                .setDate(addDietRecordStart.getDate())
                .setTime(addDietRecordStart.getTime())
                .setClient(clientEntity)
                .setProduct(productEntity);

        DietRecordBean dietRecordBean = new DietRecordBean(dietRecordRepository.save(dietRecordEntity));
        return new AddDietRecordFinish()
                .setProductTitle(productEntity.getTitle())
                .setProductUuid(productUuid)
                .setProteins(dietRecordBean.getProteins())
                .setFats(dietRecordBean.getFats())
                .setCarbohydrates(dietRecordBean.getCarbohydrates())
                .setCalories(dietRecordBean.getCalories());
    }

    public DietDisplay display(Display display) {
        Preconditions.checkNotNull(display, "display is null");

        String clientUuid = display.getClientUuid();
        clientRepository
                .findByUuid(clientUuid)
                .orElseThrow(() -> new ClientNotFoundException("Client with uuid = [" + clientUuid + "] not found!"));

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

    public List<DietRecordExport> findByDateRange(String clientUuid,
                                                  LocalDate startDate,
                                                  LocalDate endDate) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        Preconditions.checkNotNull(startDate, "startDate is null");
        Preconditions.checkNotNull(endDate, "endDate is null");
        return dietRecordRepository.
                findByDateRange(clientUuid, startDate, endDate)
                .stream()
                .map(DietRecordBean::new)
                .map(DietRecordExport::new)
                .collect(Collectors.toList());
    }
}
