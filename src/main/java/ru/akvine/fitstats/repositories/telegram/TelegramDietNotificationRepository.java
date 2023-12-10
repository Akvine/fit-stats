package ru.akvine.fitstats.repositories.telegram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.entities.telegram.TelegramDietNotificationSubscriptionEntity;
import ru.akvine.fitstats.enums.telegram.DietNotificationSubscriptionType;

import java.util.List;
import java.util.Optional;

public interface TelegramDietNotificationRepository extends JpaRepository<TelegramDietNotificationSubscriptionEntity, Long> {
    @Query("from TelegramDietNotificationSubscriptionEntity tdne where tdne.dietNotificationSubscriptionType = :type and tdne.client.id = :clientId")
    Optional<TelegramDietNotificationSubscriptionEntity> findByClientIdAndType(@Param("clientId") Long clientId, @Param("type") DietNotificationSubscriptionType type);

    @Query("from TelegramDietNotificationSubscriptionEntity tdne where tdne.client.id = :clientId and tdne.client.deleted = false")
    List<TelegramDietNotificationSubscriptionEntity> findByClientId(@Param("clientId") Long clientId);

    @Query("delete from TelegramDietNotificationSubscriptionEntity tdne where tdne.client.id = :clientId and tdne.dietNotificationSubscriptionType = :type")
    @Modifying
    @Transactional
    void deleteByClientIdAndType(@Param("clientId") Long clientId, @Param("type") DietNotificationSubscriptionType type);
}