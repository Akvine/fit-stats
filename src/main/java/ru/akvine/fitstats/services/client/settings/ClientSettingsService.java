package ru.akvine.fitstats.services.client.settings;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.ClientSettingsEntity;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;
import ru.akvine.fitstats.exceptions.client.ClientSettingsNotFoundException;
import ru.akvine.fitstats.repositories.ClientSettingsRepository;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientSettingsService {
    private final ClientSettingsRepository clientSettingsRepository;
    private static final int DEFAULT_ROUND_ACCURACY = 1;

    public ClientSettingsBean create(ClientEntity clientEntity) {
        Preconditions.checkNotNull(clientEntity, "clientEntity is null");
        String email = clientEntity.getEmail();
        logger.debug("Create settings for client with email = {}", email);

        ClientSettingsEntity clientSettingsEntity = new ClientSettingsEntity()
                .setLanguage(Language.EN)
                .setRoundAccuracy(DEFAULT_ROUND_ACCURACY)
                .setPrintMacronutrientsMode(PrintMacronutrientsMode.DEFAULT)
                .setClientEntity(clientEntity);

        ClientSettingsEntity updatedClientSettings = clientSettingsRepository.save(clientSettingsEntity);
        logger.debug("Success save settings for client with email = {}", email);
        return new ClientSettingsBean(updatedClientSettings);
    }

    public ClientSettingsBean update(ClientSettingsBean clientSettingsBean) {
        Preconditions.checkNotNull(clientSettingsBean, "clientSettingsBean is null");

        String email = clientSettingsBean.getClientEmail();
        logger.debug("Update settings for client with email = {}", email);

        ClientSettingsEntity clientSettingsEntity = findEntityByClientEmail(email);
        if (clientSettingsBean.getLanguage() != null) {
            clientSettingsEntity.setLanguage(clientSettingsBean.getLanguage());
        }

        if (clientSettingsBean.getPrintMacronutrientsMode() != null) {
            clientSettingsEntity.setPrintMacronutrientsMode(clientSettingsBean.getPrintMacronutrientsMode());
        }

        if (clientSettingsBean.getRoundAccuracy() != null) {
            clientSettingsEntity.setRoundAccuracy(clientSettingsBean.getRoundAccuracy());
        }

        clientSettingsEntity.setUpdatedDate(LocalDateTime.now());

        ClientSettingsBean updatedClientSettings = new ClientSettingsBean(clientSettingsRepository.save(clientSettingsEntity));
        logger.debug("Successful update settings for client with email = {}", email);
        return updatedClientSettings;
    }

    public ClientSettingsBean findBeanByClientEmail(String email) {
        Preconditions.checkNotNull(email, "email is null");
        return new ClientSettingsBean(findEntityByClientEmail(email));
    }

    public ClientSettingsEntity findEntityByClientEmail(String email) {
        return clientSettingsRepository
                .findByEmail(email)
                .orElseThrow(() -> new ClientSettingsNotFoundException("Settings for client with email = [" + email + "] not found!"));
    }

    public ClientSettingsBean getByClientEmail(String key) {
        return findBeanByClientEmail(key);
    }
}
