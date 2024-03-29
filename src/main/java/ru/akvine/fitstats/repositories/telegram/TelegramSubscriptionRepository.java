package ru.akvine.fitstats.repositories.telegram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.telegram.TelegramSubscriptionEntity;

import java.util.Optional;

public interface TelegramSubscriptionRepository extends JpaRepository<TelegramSubscriptionEntity, Long> {
    @Query("from TelegramSubscriptionEntity tse where tse.telegramId = :telegramId " +
            "and tse.deleted = false " +
            "and tse.client.deleted = false")
    Optional<TelegramSubscriptionEntity> findByTelegramId(@Param("telegramId") Long telegramId);

    @Query("from TelegramSubscriptionEntity t where t.telegramId = :telegramId " +
            "and t.type.id = :typeId " +
            "and t.deleted = false " +
            "and t.client.deleted = false")
    Optional<TelegramSubscriptionEntity> findByTelegramIdAndTypeId(
            @Param("telegramId") Long telegramId,
            @Param("typeId") Long typeId);

    @Query("from TelegramSubscriptionEntity tse where tse.client.id = :clientId " +
            "and tse.deleted = false " +
            "and tse.client.deleted = false")
    Optional<TelegramSubscriptionEntity> findByClientId(@Param("clientId") Long clientId);
}
