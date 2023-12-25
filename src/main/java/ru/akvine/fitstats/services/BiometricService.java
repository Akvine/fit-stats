package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.BiometricEntity;
import ru.akvine.fitstats.exceptions.profile.BiometricNotFoundException;
import ru.akvine.fitstats.repositories.BiometricRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BiometricService {
    private final BiometricRepository biometricRepository;

    public BiometricEntity verifyExistsAndGet(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        logger.info("Verify biometric entity data exists for client with uuid = {}", clientUuid);
        return biometricRepository
                .findByClientUuid(clientUuid)
                .orElseThrow(() -> new BiometricNotFoundException("Biometric with client uuid = [" + clientUuid + "] not found!"));
    }
}
