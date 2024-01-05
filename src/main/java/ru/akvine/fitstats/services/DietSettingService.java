package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.exceptions.diet.DietSettingNotFoundException;
import ru.akvine.fitstats.repositories.DietSettingRepository;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.diet.DietSettingBean;
import ru.akvine.fitstats.services.dto.diet.UpdateDietSetting;
import ru.akvine.fitstats.services.dto.diet.ChangeDiet;
import ru.akvine.fitstats.utils.DietUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class DietSettingService {
    private final DietSettingRepository dietSettingRepository;

    public DietSettingBean add(ClientEntity clientEntity, Macronutrients macronutrients, Diet diet) {
        Preconditions.checkNotNull(clientEntity, "clientEntity is null");
        Preconditions.checkNotNull(macronutrients, "macronutrients is null");
        Preconditions.checkNotNull(diet, "diet is null");
        logger.info("Save diet setting for client with uuid = {}", clientEntity.getUuid());

        DietSettingEntity dietSettingEntity = new DietSettingEntity()
                .setDiet(diet)
                .setMaxCalories(macronutrients.getCalories())
                .setMaxProteins(macronutrients.getProteins())
                .setMaxFats(macronutrients.getFats())
                .setMaxCarbohydrates(macronutrients.getCarbohydrates())
                .setClient(clientEntity);

        DietSettingEntity savedDietSettingEntity = dietSettingRepository.save(dietSettingEntity);
        logger.info("Successful save diet setting = [{}]", savedDietSettingEntity);
        return new DietSettingBean(savedDietSettingEntity);
    }

    public DietSettingBean update(UpdateDietSetting updateDietSetting) {
        Preconditions.checkNotNull(updateDietSetting, "updateDietSetting is null");
        logger.info("Update diet setting by = [{}]", updateDietSetting);

        DietSettingEntity dietSettingEntity = (DietSettingEntity) verifyExistsAndGet(updateDietSetting.getClientUuid())
                .setMaxProteins(updateDietSetting.getMaxProteins())
                .setMaxFats(updateDietSetting.getMaxFats())
                .setMaxCarbohydrates(updateDietSetting.getMaxCarbohydrates())
                .setMaxCalories(updateDietSetting.getMaxCalories())
                .setUpdatedDate(LocalDateTime.now());

        DietSettingEntity savedDietSettingEntity = dietSettingRepository.save(dietSettingEntity);
        logger.info("Successful save diet setting entity = [{}]", dietSettingEntity);
        return new DietSettingBean(savedDietSettingEntity);
    }

    public void changeDiet(BiometricBean biometricBean, ChangeDiet changeDiet) {
        Preconditions.checkNotNull(changeDiet, "updateDiet is null");
        logger.info("Update diet with type = {} for client with uuid = {}", changeDiet.getDiet(), changeDiet.getClientUuid());

        Diet diet = changeDiet.getDiet();
        String clientUuid = changeDiet.getClientUuid();
        Macronutrients macronutrients = DietUtils.calculate(biometricBean, diet);

        DietSettingEntity dietSettingEntity = (DietSettingEntity) verifyExistsAndGet(clientUuid)
                .setMaxProteins(macronutrients.getProteins())
                .setMaxFats(macronutrients.getFats())
                .setMaxCarbohydrates(macronutrients.getCarbohydrates())
                .setMaxCalories(macronutrients.getCalories())
                .setDiet(diet)
                .setUpdatedDate(LocalDateTime.now());

        dietSettingRepository.save(dietSettingEntity);
        logger.info("Successful update diet for client with uuid = {}", clientUuid);
    }

    public void deleteForClient(Long clientId) {
        Preconditions.checkNotNull(clientId, "clientId is null");
        logger.info("Delete diet setting for client with id = {}", clientId);
        dietSettingRepository.deleteForClientWithId(clientId);
    }

    public DietSettingEntity verifyExistsAndGet(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        logger.info("Verify diet setting entity data exists for client with uuid = {}", clientUuid);
        return dietSettingRepository.findByClientUuid(clientUuid)
                .orElseThrow(() -> new DietSettingNotFoundException("Diet setting with client uuid = [" + clientUuid + "] not found!"));
    }
}
