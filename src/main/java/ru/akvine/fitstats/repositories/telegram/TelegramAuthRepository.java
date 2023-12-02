package ru.akvine.fitstats.repositories.telegram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.entities.telegram.TelegramAuthCodeEntity;

import java.util.Optional;

public interface TelegramAuthRepository extends JpaRepository<TelegramAuthCodeEntity, Long> {
    @Query("from TelegramAuthCodeEntity tace where tace.code = :authCode")
    Optional<TelegramAuthCodeEntity> findByAuthCode(@Param("authCode") String authCode);

    @Query("from TelegramAuthCodeEntity tace where tace.client.uuid = :clientUuid " +
            "and tace.client.deleted = false " +
            "and tace.client.deletedDate is null")
    Optional<TelegramAuthCodeEntity> findByClientUuid(@Param("clientUuid") String clientUuid);

    @Query("delete from TelegramAuthCodeEntity tace where tace.client.id = :clientId")
    @Modifying
    @Transactional
    void deleteAllByClientId(@Param("clientId") long clientId);
}
