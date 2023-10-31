package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.BiometricEntity;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.fitstats.exceptions.client.ClientNotFoundException;
import ru.akvine.fitstats.exceptions.security.AuthenticationException;
import ru.akvine.fitstats.repositories.BiometricRepository;
import ru.akvine.fitstats.repositories.ClientRepository;
import ru.akvine.fitstats.repositories.DietSettingRepository;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.Macronutrients;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.client.ClientRegister;
import ru.akvine.fitstats.utils.SecurityUtils;
import ru.akvine.fitstats.utils.UUIDGenerator;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    @Value("${uuid.length}")
    private int length;

    private final ClientRepository clientRepository;
    private final BiometricRepository biometricRepository;
    private final DietSettingRepository dietSettingRepository;
    private final PasswordService passwordService;
    private final DietService dietService;

    public ClientBean register(ClientRegister clientRegister) {
        Preconditions.checkNotNull(clientRegister, "clientRegister is null");

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
        BiometricEntity biometricEntity = new BiometricEntity()
                .setAge(clientRegister.getAge())
                .setGender(clientRegister.getGender())
                .setHeight(clientRegister.getHeight())
                .setWeight(clientRegister.getWeight())
                .setPhysicalActivity(clientRegister.getPhysicalActivity())
                .setHeightMeasurement(clientRegister.getHeightMeasurement())
                .setWeightMeasurement(clientRegister.getWeightMeasurement())
                .setClientEntity(savedClientEntity);
        Macronutrients macronutrients = dietService.calculate(
                new BiometricBean(biometricEntity), diet);
        DietSettingEntity dietSettingEntity = new DietSettingEntity()
                .setDiet(diet)
                .setMaxCalories(macronutrients.getCalories())
                .setMaxProteins(macronutrients.getProteins())
                .setMaxFats(macronutrients.getFats())
                .setMaxCarbohydrates(macronutrients.getCarbohydrates())
                .setClient(savedClientEntity);
        biometricRepository.save(biometricEntity);
        dietSettingRepository.save(dietSettingEntity);

        return new ClientBean(savedClientEntity);
    }

    public void login(ClientBean loginClientBean) {
        Preconditions.checkNotNull(loginClientBean, "loginClientBean is null");

        ClientBean clientBean = new ClientBean(verifyExistsByEmailAndGet(loginClientBean.getEmail()));
        String password = loginClientBean.getPassword();

        if (passwordService.isValidPassword(clientBean, password)) {
            SecurityUtils.authenticate(clientBean);
        } else {
            throw new AuthenticationException("Authentication failed. Email or password is invalid");
        }
    }

    public ClientBean getByUuid(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return new ClientBean(verifyExistsByEmailAndGet(uuid));
    }

    public boolean isExistsByEmail(String email) {
        Preconditions.checkNotNull(email, "email is null");
        return clientRepository.findByEmail(email).isPresent();
    }


    public ClientEntity verifyExistsByUuidAndGet(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return clientRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ClientNotFoundException("Client with uuid = [" + uuid + "] not found!"));
    }


    public ClientEntity verifyExistsByEmailAndGet(String email) {
         Preconditions.checkNotNull(email, "email is null");
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