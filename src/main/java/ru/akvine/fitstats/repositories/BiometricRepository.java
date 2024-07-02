package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.entities.BiometricEntity;

import java.util.Optional;

public interface BiometricRepository extends JpaRepository<BiometricEntity, Long> {
    @Query("from BiometricEntity be where " +
            "be.clientEntity.uuid = :clientUuid " +
            "and " +
            "be.clientEntity.deleted = false")
    Optional<BiometricEntity> findByClientUuid(@Param("clientUuid") String clientUuid);

    @Modifying
    @Transactional
    @Query("delete from BiometricEntity be where be.clientEntity.id = :id")
    void deleteForClientWithId(@Param("id") Long id);
}
