package ru.akvine.fitstats.services.telegram;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.telegram.TelegramSubscriptionEntity;
import ru.akvine.fitstats.entities.telegram.TelegramSubscriptionTypeEntity;
import ru.akvine.fitstats.exceptions.telegram.TelegramSubscriptionNotFoundException;
import ru.akvine.fitstats.repositories.telegram.TelegramSubscriptionRepository;
import ru.akvine.fitstats.services.ClientService;
import ru.akvine.fitstats.services.dto.telegram.TelegramAuthCode;
import ru.akvine.fitstats.services.dto.telegram.TelegramSubscriptionBean;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramSubscriptionService {
    private final ClientService clientService;
    private final TelegramSubscriptionTypeService telegramSubscriptionTypeService;
    private final TelegramSubscriptionRepository telegramSubscriptionRepository;

    public TelegramSubscriptionBean findByTelegramId(Long telegramId) {
        Preconditions.checkNotNull(telegramId, "telegramId is null");

        return new TelegramSubscriptionBean(
          telegramSubscriptionRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new TelegramSubscriptionNotFoundException("No telegram subscription found with telegramId = [" + telegramId + "]"))
        );
    }

    public TelegramSubscriptionBean findByTelegramIdAndTypeId(
            Long telegramId,
            Long subscriptionTypeId) throws TelegramSubscriptionNotFoundException {
        Preconditions.checkNotNull(telegramId, "telegramId is null");
        TelegramSubscriptionTypeEntity subscriptionType = telegramSubscriptionTypeService.getById(subscriptionTypeId);

        return new TelegramSubscriptionBean(
                telegramSubscriptionRepository.findByTelegramIdAndTypeId(telegramId, subscriptionType.getId())
                        .orElseThrow(() -> new TelegramSubscriptionNotFoundException("No telegram subscription found with telegramId=" + telegramId))
        );
    }

    public TelegramSubscriptionBean save(Long telegramId, TelegramAuthCode authCode, String chatId) {
        Preconditions.checkNotNull(telegramId, "telegramId is null");
        Preconditions.checkNotNull(authCode, "authCode is null");
        Preconditions.checkNotNull(chatId, "chatId is null");
        ClientEntity client = clientService.verifyExistsByUuidAndGet(authCode.getClient().getUuid());

        Optional<TelegramSubscriptionEntity> subscriptionOptional = telegramSubscriptionRepository
                .findByTelegramId(telegramId);

        if (subscriptionOptional.isPresent()) {
            return new TelegramSubscriptionBean(subscriptionOptional.get());
        }

        TelegramSubscriptionEntity telegramSubscription = new TelegramSubscriptionEntity()
                .setTelegramId(telegramId)
                .setClient(client)
                .setChatId(chatId);
        return new TelegramSubscriptionBean(telegramSubscriptionRepository.save(telegramSubscription));
    }

    public void delete(Long telegramId, Long subscriptionTypeId) {
        Preconditions.checkNotNull(telegramId, "telegramId is null");
        TelegramSubscriptionTypeEntity subscriptionType = telegramSubscriptionTypeService.getById(subscriptionTypeId);
        logger.debug("Deleting telegram subscription for telegramId=[{}]", telegramId);

        TelegramSubscriptionEntity subscriptionEntity = telegramSubscriptionRepository
                .findByTelegramIdAndTypeId(telegramId, subscriptionType.getId())
                .orElseThrow(() -> new TelegramSubscriptionNotFoundException(
                        String.format("No telegram subscription with telegramId = [%s]", telegramId)
                ));

        subscriptionEntity.setDeleted(true);
        subscriptionEntity.setDeletedDate(LocalDateTime.now());
        telegramSubscriptionRepository.save(subscriptionEntity);
    }
}
