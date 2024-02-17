package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.telegram.TelegramSubscriptionTypeEntity;
import ru.akvine.fitstats.exceptions.telegram.TelegramSubscriptionTypeNotFoundException;
import ru.akvine.fitstats.repositories.telegram.TelegramSubscriptionTypeRepository;

@Service
@RequiredArgsConstructor
public class TelegramSubscriptionTypeService {
    private final TelegramSubscriptionTypeRepository telegramSubscriptionTypeRepository;

    public TelegramSubscriptionTypeEntity getById(Long id) {
        return telegramSubscriptionTypeRepository.findById(id)
                .orElseThrow(() -> new TelegramSubscriptionTypeNotFoundException("Check subscription type table! Not found subscriptionTypeId=" + id));
    }
}
