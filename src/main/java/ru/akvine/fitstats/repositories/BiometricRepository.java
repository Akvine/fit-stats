package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.BiometricEntity;

import java.util.Optional;

public interface BiometricRepository extends JpaRepository<BiometricEntity, Long> {
    @Query("from BiometricEntity be where " +
            "be.clientEntity.uuid = :clientUuid " +
            "and " +
            "be.clientEntity.deleted = false and be.clientEntity.deletedDate is null")
    Optional<BiometricEntity> findByClientUuid(@Param("clientUuid") String clientUuid);
}
