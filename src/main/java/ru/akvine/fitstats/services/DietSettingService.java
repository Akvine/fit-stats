package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.exceptions.diet.DietSettingNotFoundException;
import ru.akvine.fitstats.repositories.DietSettingRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class DietSettingService {
    private final DietSettingRepository dietSettingRepository;

    public DietSettingEntity verifyExistsAndGet(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        logger.info("Verify diet setting entity data exists for client with uuid = {}", clientUuid);
        return dietSettingRepository.findByClientUuid(clientUuid)
                .orElseThrow(() -> new DietSettingNotFoundException("Diet setting with client uuid = [" + clientUuid + "] not found!"));
    }
}
