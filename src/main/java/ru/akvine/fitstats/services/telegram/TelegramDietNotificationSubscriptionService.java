package ru.akvine.fitstats.services.telegram;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.entities.telegram.TelegramDietNotificationSubscriptionEntity;
import ru.akvine.fitstats.enums.telegram.DietNotificationSubscriptionType;
import ru.akvine.fitstats.exceptions.client.ClientNotFoundException;
import ru.akvine.fitstats.exceptions.telegram.TelegramSubscriptionNotFoundException;
import ru.akvine.fitstats.repositories.ClientRepository;
import ru.akvine.fitstats.repositories.telegram.TelegramDietNotificationRepository;
import ru.akvine.fitstats.repositories.telegram.TelegramSubscriptionRepository;
import ru.akvine.fitstats.services.dto.telegram.AddDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.DeleteDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.TelegramDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.TelegramSubscriptionBean;
import ru.akvine.fitstats.services.listeners.SendMessageEvent;
import ru.akvine.fitstats.utils.MathUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelegramDietNotificationSubscriptionService {
    private final TelegramDietNotificationRepository telegramDietNotificationSubscriptionRepository;
    private final TelegramSubscriptionRepository telegramSubscriptionRepository;
    private final ClientRepository clientRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<TelegramDietNotificationSubscription> list(TelegramBaseRequest request) {
        Preconditions.checkNotNull(request, "telegramBaseRequest is null");
        Long clientId = verifyExistsByUuidAndGet(request.getClientUuid()).getId();
        return telegramDietNotificationSubscriptionRepository
                .findByClientId(clientId)
                .stream()
                .map(TelegramDietNotificationSubscription::new)
                .collect(Collectors.toList());
    }

    public List<TelegramDietNotificationSubscription> list(Long clientId) {
        Preconditions.checkNotNull(clientId, "clientId is null");
        return telegramDietNotificationSubscriptionRepository
                .findByClientId(clientId)
                .stream()
                .map(TelegramDietNotificationSubscription::new)
                .collect(Collectors.toList());
    }

    public void add(AddDietNotificationSubscription addDietNotificationSubscription) {
        Preconditions.checkNotNull(addDietNotificationSubscription, "addDietNotification is null");

        ClientEntity clientEntity = verifyExistsByUuidAndGet(addDietNotificationSubscription.getClientUuid());
        TelegramDietNotificationSubscriptionEntity telegramDietNotificationSubscriptionEntity = TelegramDietNotificationSubscriptionEntity.builder()
                .telegramId(addDietNotificationSubscription.getTelegramId())
                .client(clientEntity)
                .dietNotificationSubscriptionType(addDietNotificationSubscription.getType())
                .build();

        telegramDietNotificationSubscriptionRepository.save(telegramDietNotificationSubscriptionEntity);
    }

    public void notifyIfNeed(Long clientId, DietSettingEntity dietSettingEntity, List<DietRecordEntity> records) {
        Preconditions.checkNotNull(clientId, "clientId is null");
        Preconditions.checkNotNull(dietSettingEntity, "dietSettingEntity is null");
        Preconditions.checkNotNull(records, "dietRecords is null");
        TelegramSubscriptionBean telegramSubscription;
        try {
            telegramSubscription = new TelegramSubscriptionBean(telegramSubscriptionRepository
                    .findByClientId(clientId)
                    .orElseThrow(() -> new TelegramSubscriptionNotFoundException("TelegramSubscription not found!")));
        } catch (TelegramSubscriptionNotFoundException exception) {
            return;
        }

        double maxCalories = dietSettingEntity.getMaxCalories();
        double maxProteins = dietSettingEntity.getMaxProteins();
        double maxFats = dietSettingEntity.getMaxFats();
        double maxCarbohydrates = dietSettingEntity.getMaxCarbohydrates();
        double currentCalories = records.stream().mapToDouble(DietRecordEntity::getCalories).sum();
        double currentProteins = records.stream().mapToDouble(DietRecordEntity::getProteins).sum();
        double currentFats = records.stream().mapToDouble(DietRecordEntity::getFats).sum();
        double currentCarbohydrates = records.stream().mapToDouble(DietRecordEntity::getCarbohydrates).sum();

        List<TelegramDietNotificationSubscriptionEntity> subscriptions = verifyExistsAndGet(clientId);
        TelegramDietNotificationSubscriptionEntity fatsSubscription = subscriptions
                .stream()
                .filter(subscription -> subscription.getDietNotificationSubscriptionType().equals(DietNotificationSubscriptionType.FATS))
                .findFirst()
                .orElse(null);
        if (fatsSubscription != null && currentFats > maxFats && !fatsSubscription.isProcessed()) {
            fatsSubscription.setProcessed(true);
            telegramDietNotificationSubscriptionRepository.save(fatsSubscription);
            String message = String.format("Превышен лимит по потреблению жиров: %s/%s", MathUtils.round(currentFats), MathUtils.round(maxFats));
            SendMessage sendMessage = new SendMessage(telegramSubscription.getChatId(),
                    message);
            applicationEventPublisher.publishEvent(new SendMessageEvent(new Object(), sendMessage));
        }

        TelegramDietNotificationSubscriptionEntity proteinsSubscription = subscriptions
                .stream()
                .filter(subscription -> subscription.getDietNotificationSubscriptionType().equals(DietNotificationSubscriptionType.PROTEINS))
                .findFirst()
                .orElse(null);
        if (proteinsSubscription != null && currentProteins > maxProteins && !proteinsSubscription.isProcessed()) {
            proteinsSubscription.setProcessed(true);
            telegramDietNotificationSubscriptionRepository.save(proteinsSubscription);
            String message = String.format("Превышен лимит по потреблению белков: %s/%s", MathUtils.round(currentProteins), MathUtils.round(maxProteins));
            SendMessage sendMessage = new SendMessage(telegramSubscription.getChatId(),
                    message);
            applicationEventPublisher.publishEvent(new SendMessageEvent(new Object(), sendMessage));
        }

        TelegramDietNotificationSubscriptionEntity carbohydratesSubscription = subscriptions
                .stream()
                .filter(subscription -> subscription.getDietNotificationSubscriptionType().equals(DietNotificationSubscriptionType.CARBOHYDRATES))
                .findFirst()
                .orElse(null);
        if (carbohydratesSubscription != null && currentCarbohydrates > maxCarbohydrates && !carbohydratesSubscription.isProcessed()) {
            carbohydratesSubscription.setProcessed(true);
            telegramDietNotificationSubscriptionRepository.save(carbohydratesSubscription);
            String message = String.format("Превышен лимит по потреблению углеводов: %s/%s", MathUtils.round(currentCarbohydrates), MathUtils.round(maxCarbohydrates));
            SendMessage sendMessage = new SendMessage(telegramSubscription.getChatId(),
                    message);
            applicationEventPublisher.publishEvent(new SendMessageEvent(new Object(), sendMessage));
        }

        TelegramDietNotificationSubscriptionEntity energySubscription = subscriptions
                .stream()
                .filter(subscription -> subscription.getDietNotificationSubscriptionType().equals(DietNotificationSubscriptionType.ENERGY))
                .findFirst()
                .orElse(null);
        if (energySubscription != null && currentCalories > maxCalories && !energySubscription.isProcessed()) {
            energySubscription.setProcessed(true);
            telegramDietNotificationSubscriptionRepository.save(energySubscription);
            String message = String.format("Превышен лимит по потреблению калорий: %s/%s", MathUtils.round(currentCalories), MathUtils.round(maxCalories));
            SendMessage sendMessage = new SendMessage(telegramSubscription.getChatId(),
                    message);
            applicationEventPublisher.publishEvent(new SendMessageEvent(new Object(), sendMessage));
        }
    }

    public void delete(DeleteDietNotificationSubscription deleteDietNotificationSubscription) {
        Preconditions.checkNotNull(deleteDietNotificationSubscription, "deleteDietNotificationSubscription is null");
        ClientEntity clientEntity = verifyExistsByUuidAndGet(deleteDietNotificationSubscription.getClientUuid());
        telegramDietNotificationSubscriptionRepository.deleteByClientIdAndType(clientEntity.getId(), deleteDietNotificationSubscription.getType());
    }

    public boolean isExistsByClientIdAndType(Long clientId, DietNotificationSubscriptionType type) {
        Preconditions.checkNotNull(clientId, "clientId is null");
        Preconditions.checkNotNull(type, "dietNotificationType is null");
        return telegramDietNotificationSubscriptionRepository.findByClientIdAndType(clientId, type).isPresent();
    }

    public List<TelegramDietNotificationSubscriptionEntity> verifyExistsAndGet(Long clientId) {
        Preconditions.checkNotNull(clientId, "clientId is null");
        return telegramDietNotificationSubscriptionRepository
                .findByClientId(clientId)
                .stream()
                .collect(Collectors.toList());
    }

    public ClientEntity verifyExistsByUuidAndGet(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return clientRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ClientNotFoundException("Client with uuid = [" + uuid + "] not found!"));
    }
}
