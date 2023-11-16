package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.BiometricEntity;
import ru.akvine.fitstats.exceptions.profile.BiometricNotFoundException;
import ru.akvine.fitstats.repositories.BiometricRepository;

@Service
@RequiredArgsConstructor
public class BiometricService {
    private final BiometricRepository biometricRepository;

    public BiometricEntity verifyExistsAndGet(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        return biometricRepository
                .findByClientUuid(clientUuid)
                .orElseThrow(() -> new BiometricNotFoundException("Biometric with client uuid = [" + clientUuid + "] not found!"));
    }
}
