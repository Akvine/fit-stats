package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.entities.DietSettingEntity;

import java.util.Optional;

public interface DietSettingRepository extends JpaRepository<DietSettingEntity, Long> {
    @Query("from DietSettingEntity dse where dse.client.uuid = :clientUuid " +
            "and " +
            "dse.deleted = false " +
            "and " +
            "dse.client.deleted = false")
    Optional<DietSettingEntity> findByClientUuid(@Param("clientUuid") String clientUuid);

    @Transactional
    @Modifying
    @Query("delete from DietSettingEntity dse where dse.client.id = :id")
    void deleteForClientWithId(@Param("id") Long id);
}
