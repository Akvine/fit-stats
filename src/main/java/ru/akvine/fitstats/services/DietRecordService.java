package ru.akvine.fitstats.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.repositories.DietRecordRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietRecordService {
    private final DietRecordRepository dietRecordRepository;

    public void deleteAllForClient(Long clientId) {
        logger.info("Delete all diet records for client with id = {}", clientId);
        dietRecordRepository.deleteAllForClientWithId(clientId);
    }
}
