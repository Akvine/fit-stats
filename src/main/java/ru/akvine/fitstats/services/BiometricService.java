package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.BiometricEntity;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.exceptions.profile.BiometricNotFoundException;
import ru.akvine.fitstats.repositories.BiometricRepository;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.diet.UpdateDietSetting;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.utils.DietUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BiometricService {
    private final BiometricRepository biometricRepository;
    private final DietSettingService dietSettingService;

    public BiometricBean add(BiometricBean biometricBean, ClientEntity clientEntity) {
        Preconditions.checkNotNull(biometricBean, "biometricBean is null");
        Preconditions.checkNotNull(clientEntity, "clientEntity is null");
        logger.info("Successful save biometric data for client = [{}]", clientEntity);

        BiometricEntity biometricEntity = new BiometricEntity()
                .setAge(biometricBean.getAge())
                .setGender(biometricBean.getGender())
                .setHeight(biometricBean.getHeight())
                .setWeight(biometricBean.getWeight())
                .setPhysicalActivity(biometricBean.getPhysicalActivity())
                .setHeightMeasurement(biometricBean.getHeightMeasurement())
                .setWeightMeasurement(biometricBean.getWeightMeasurement())
                .setClientEntity(clientEntity);

        BiometricEntity savedBiometricEntity = biometricRepository.save(biometricEntity);
        logger.info("Successful save biometric entity = [{}]", savedBiometricEntity);
        return new BiometricBean(savedBiometricEntity);
    }

    public BiometricBean update(UpdateBiometric updateBiometric) {
        Preconditions.checkNotNull(updateBiometric, "updateBiometric is null");
        logger.info("Update biometric for client with uuid = {}", updateBiometric.getClientUuid());

        String clientUuid = updateBiometric.getClientUuid();
        BiometricEntity biometricEntity = verifyExistsAndGet(clientUuid);
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
            Macronutrients macronutrients = DietUtils.calculate(savedBiometricBean, dietSettingEntity.getDiet());
            UpdateDietSetting updateDietSetting = new UpdateDietSetting()
                    .setClientUuid(clientUuid)
                    .setMaxProteins(macronutrients.getProteins())
                    .setMaxFats(macronutrients.getFats())
                    .setMaxCarbohydrates(macronutrients.getCarbohydrates())
                    .setMaxCalories(macronutrients.getCalories());
            dietSettingService.update(updateDietSetting);
        }

        logger.info("Successful update biometric data for client with uuid = {}", clientUuid);
        return savedBiometricBean;
    }

    public BiometricBean getByClientUuid(String clientUuid) {
        return new BiometricBean(verifyExistsAndGet(clientUuid));
    }

    public void deleteForClient(Long clientId) {
        Preconditions.checkNotNull(clientId, "clientId is null");
        logger.info("Delete biometric data for client with id = {}", clientId);
        biometricRepository.deleteForClientWithId(clientId);
    }

    public BiometricEntity verifyExistsAndGet(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        logger.info("Verify biometric entity data exists for client with uuid = {}", clientUuid);
        return biometricRepository
                .findByClientUuid(clientUuid)
                .orElseThrow(() -> new BiometricNotFoundException("Biometric with client uuid = [" + clientUuid + "] not found!"));
    }
}
