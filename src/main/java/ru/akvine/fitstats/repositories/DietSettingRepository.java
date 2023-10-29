package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.DietSettingEntity;

import java.util.Optional;

public interface DietSettingRepository extends JpaRepository<DietSettingEntity, Long> {
    @Query("from DietSettingEntity dse where dse.client.uuid = :clientUuid " +
            "and " +
            "dse.deleted = false and dse.deletedDate is null " +
            "and " +
            "dse.client.deleted = false and dse.client.deletedDate is null")
    Optional<DietSettingEntity> findByClientUuid(@Param("clientUuid") String clientUuid);
}
