package ru.akvine.fitstats.services.telegram;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.telegram.TelegramSubscriptionEntity;
import ru.akvine.fitstats.exceptions.telegram.TelegramSubscriptionNotFoundException;
import ru.akvine.fitstats.repositories.telegram.TelegramSubscriptionRepository;
import ru.akvine.fitstats.services.ClientService;
import ru.akvine.fitstats.services.dto.telegram.TelegramAuthCode;
import ru.akvine.fitstats.services.dto.telegram.TelegramSubscriptionBean;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramSubscriptionService {
    private final ClientService clientService;
    private final TelegramSubscriptionRepository telegramSubscriptionRepository;

    public TelegramSubscriptionBean findByTelegramId(Long telegramId) {
        Preconditions.checkNotNull(telegramId, "telegramId is null");

        return new TelegramSubscriptionBean(
          telegramSubscriptionRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new TelegramSubscriptionNotFoundException("No telegram subscription found with telegramId = [" + telegramId + "]"))
        );
    }

    public TelegramSubscriptionBean save(Long telegramId, TelegramAuthCode authCode) {
        Preconditions.checkNotNull(telegramId, "telegramId is null");
        Preconditions.checkNotNull(authCode, "authCode is null");
        ClientEntity client = clientService.verifyExistsByUuidAndGet(authCode.getClient().getUuid());

        Optional<TelegramSubscriptionEntity> subscriptionOptional = telegramSubscriptionRepository
                .findByTelegramId(telegramId);

        if (subscriptionOptional.isPresent()) {
            return new TelegramSubscriptionBean(subscriptionOptional.get());
        }

        TelegramSubscriptionEntity telegramSubscription = new TelegramSubscriptionEntity()
                .setTelegramId(telegramId)
                .setClient(client);
        return new TelegramSubscriptionBean(telegramSubscriptionRepository.save(telegramSubscription));
    }

    public void delete(Long telegramId) {
        Preconditions.checkNotNull(telegramId, "telegramId is null");

        TelegramSubscriptionEntity subscriptionEntity = telegramSubscriptionRepository
                .findByTelegramId(telegramId)
                .orElseThrow(() -> new TelegramSubscriptionNotFoundException(
                        String.format("No telegram subscription with telegramId = [%s]", telegramId)
                ));

        subscriptionEntity.setDeleted(true);
        subscriptionEntity.setDeletedDate(LocalDateTime.now());
        telegramSubscriptionRepository.save(subscriptionEntity);
    }
}
