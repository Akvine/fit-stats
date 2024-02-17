package ru.akvine.fitstats.repositories.telegram;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.akvine.fitstats.entities.telegram.TelegramSubscriptionTypeEntity;

public interface TelegramSubscriptionTypeRepository extends JpaRepository<TelegramSubscriptionTypeEntity, Long> {
}
