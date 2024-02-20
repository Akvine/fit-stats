package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.WeightRecordEntity;
import ru.akvine.fitstats.exceptions.weight.WeightRecordNotFoundException;
import ru.akvine.fitstats.repositories.WeightRecordRepository;
import ru.akvine.fitstats.services.client.ClientService;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.services.dto.weight.ChangeWeight;
import ru.akvine.fitstats.services.dto.weight.ListWeightResult;
import ru.akvine.fitstats.services.profile.ProfileService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeightService {
    private final ProfileService profileService;
    private final WeightRecordRepository weightRecordRepository;
    private final ClientService clientService;

    public ListWeightResult list(String clientUuid) {
        logger.info("List weights for client with uuid = {}", clientUuid);
        Map<String, String> weights = weightRecordRepository
                .findAll(clientUuid)
                .stream()
                .collect(toMap(weight -> weight.getDate().toString(), WeightRecordEntity::getValue));
        return new ListWeightResult(weights);
    }

    public void change(ChangeWeight changeWeight) {
        Preconditions.checkNotNull(changeWeight, "changeWeight is null");
        logger.info("Add or change weight data for client by = [{}]", changeWeight);

        String clientUuid = changeWeight.getClientUuid();
        ClientEntity clientEntity = clientService.verifyExistsByUuidAndGet(clientUuid);

        LocalDate date;
        if (changeWeight.getDate() != null) {
            date = changeWeight.getDate();
        } else {
            date = LocalDate.now();
        }

        Optional<WeightRecordEntity> weightOptional = weightRecordRepository.findByDate(date, clientUuid);
        if (weightOptional.isPresent()) {
            WeightRecordEntity weightRecord = weightOptional.get();
            weightRecord.setValue(changeWeight.getWeight());
            weightRecord.setUpdatedDate(LocalDateTime.now());
            weightRecordRepository.save(weightRecord);
        } else {
            WeightRecordEntity weightRecord = new WeightRecordEntity()
                    .setClient(clientEntity)
                    .setValue(changeWeight.getWeight())
                    .setDate(date);
            weightRecordRepository.save(weightRecord);

            UpdateBiometric updateBiometric = new UpdateBiometric()
                    .setClientUuid(clientUuid)
                    .setWeight(changeWeight.getWeight());
            profileService.updateBiometric(updateBiometric);
        }
    }

    public void delete(LocalDate date, String clientUuid) {
        logger.info("Delete weight data for client with uuid = {} and date = {}", clientUuid, date);
        LocalDate findDate = Objects.requireNonNullElseGet(date, LocalDate::now);
        WeightRecordEntity weightRecord = weightRecordRepository
                .findByDate(findDate, clientUuid)
                .orElseThrow(() -> new WeightRecordNotFoundException("Weight record not found by date = [" + findDate.toString() + "]"));
        weightRecordRepository.delete(weightRecord);
    }
}
