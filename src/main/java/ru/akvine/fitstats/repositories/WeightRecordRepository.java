package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.WeightRecordEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeightRecordRepository extends JpaRepository<WeightRecordEntity, Long> {
    @Query("from WeightRecordEntity wre where wre.date = :date and wre.client.uuid = :clientUuid and wre.client.deleted = false and wre.client.deletedDate is null")
    Optional<WeightRecordEntity> findByDate(@Param("date") LocalDate date, @Param("clientUuid") String clientUuid);

    @Query("from WeightRecordEntity wre where wre.client.uuid = :clientUuid and wre.client.deleted = false and wre.client.deletedDate is null")
    List<WeightRecordEntity> findAll(@Param("clientUuid") String clientUuid);
}
