package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.fitstats.exceptions.client.ClientNotFoundException;
import ru.akvine.fitstats.repositories.ClientRepository;
import ru.akvine.fitstats.repositories.telegram.TelegramAuthRepository;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.client.ClientRegister;
import ru.akvine.fitstats.services.telegram.TelegramAuthService;
import ru.akvine.fitstats.services.telegram.TelegramDietNotificationSubscriptionService;
import ru.akvine.fitstats.utils.DietUtils;
import ru.akvine.fitstats.utils.UUIDGenerator;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    @Value("${uuid.length}")
    private int length;

    private final ClientRepository clientRepository;
    private final BiometricService biometricService;
    private final DietSettingService dietSettingService;
    private final DietRecordService dietRecordService;
    private final PasswordService passwordService;
    private final TelegramAuthRepository telegramAuthRepository;
    private final TelegramDietNotificationSubscriptionService telegramDietNotificationSubscriptionService;

    public ClientBean register(ClientRegister clientRegister) {
        Preconditions.checkNotNull(clientRegister, "clientRegister is null");
        logger.info("Register client = [{}]", clientRegister);

        String email = clientRegister.getEmail();
        throwExceptionIfExists(email);

        String passwordHash = passwordService.encodePassword(clientRegister.getPassword());
        Diet diet = clientRegister.getDiet();

        ClientEntity clientEntity = new ClientEntity()
                .setUuid(UUIDGenerator.uuidWithoutDashes(length))
                .setEmail(clientRegister.getEmail())
                .setHash(passwordHash)
                .setFirstName(clientRegister.getFirstName())
                .setSecondName(clientRegister.getSecondName())
                .setThirdName(clientRegister.getThirdName());
        ClientEntity savedClientEntity = clientRepository.save(clientEntity);
        BiometricBean biometricBean = new BiometricBean()
                .setAge(clientRegister.getAge())
                .setGender(clientRegister.getGender())
                .setHeight(clientRegister.getHeight())
                .setWeight(clientRegister.getWeight())
                .setPhysicalActivity(clientRegister.getPhysicalActivity())
                .setHeightMeasurement(clientRegister.getHeightMeasurement())
                .setWeightMeasurement(clientRegister.getWeightMeasurement());
        BiometricBean savedBiometricBean = biometricService.add(biometricBean, savedClientEntity);
        Macronutrients macronutrients = DietUtils.calculate(
                savedBiometricBean, diet);
        dietSettingService.add(clientEntity, macronutrients, diet);

        logger.info("Successful register and save client = [{}]", savedClientEntity);
        return new ClientBean(savedClientEntity);
    }

    public ClientBean delete(String email) {
        Preconditions.checkNotNull(email, "email is null");
        logger.info("Delete client by email = {}", email);

        ClientEntity clientEntity = (ClientEntity) verifyExistsByEmailAndGet(email)
                .setDeleted(true)
                .setDeletedDate(LocalDateTime.now());
        Long clientId = clientEntity.getId();

        dietRecordService.deleteAllForClient(clientId);
        dietSettingService.deleteForClient(clientId);
        biometricService.deleteForClient(clientId);
        telegramAuthRepository.deleteAllByClientId(clientId);
        telegramDietNotificationSubscriptionService.deleteAllForClient(clientId);

        logger.info("Successful delete client with email = {}", email);
        return new ClientBean(clientRepository.save(clientEntity));
    }

    public ClientBean updatePassword(String login, String newHash) {
        logger.info("Update password for client with email = {}", login);

        ClientEntity client = verifyExistsByEmailAndGet(login);
        client.setHash(newHash);
        client.setUpdatedDate(LocalDateTime.now());

        logger.info("Successful update password for client with email = {}", login);
        return new ClientBean(clientRepository.save(client));
    }

    public ClientBean updateEmail(String login, String newEmail) {
        logger.info("Update email for client with email = {}", login);

        ClientEntity clientEntity = verifyExistsByEmailAndGet(login);
        clientEntity.setEmail(newEmail);
        clientEntity.setUpdatedDate(LocalDateTime.now());

        logger.info("Successful update email for client with email = {}", login);
        return new ClientBean(clientRepository.save(clientEntity));
    }

    public ClientBean getByUuid(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        logger.info("Try to get client with uuid = {}", uuid);
        return new ClientBean(verifyExistsByUuidAndGet(uuid));
    }

    public ClientBean getByEmail(String email) {
        Preconditions.checkNotNull(email, "email is null");
        logger.info("Try to get client with email = {}", email);
        return new ClientBean(verifyExistsByEmailAndGet(email));
    }

    public boolean isExistsByEmail(String email) {
        Preconditions.checkNotNull(email, "email is null");
        logger.info("Check client exists with email = {}", email);
        return clientRepository.findByEmail(email).isPresent();
    }


    public ClientEntity verifyExistsByUuidAndGet(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        logger.info("Check client exists with uuid and get = {}", uuid);
        return clientRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ClientNotFoundException("Client with uuid = [" + uuid + "] not found!"));
    }


    public ClientEntity verifyExistsByEmailAndGet(String email) {
        Preconditions.checkNotNull(email, "email is null");
        logger.info("Check client exists with email and get = {}", email);
        return clientRepository
                .findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Client with email = [" + email + "] not found!"));
    }

    private void throwExceptionIfExists(String email) {
        Preconditions.checkNotNull(email);
        if (isExistsByEmail(email)) {
            throw new ClientAlreadyExistsException("Client with email = [" + email + "] already exists!");
        }
    }
}
