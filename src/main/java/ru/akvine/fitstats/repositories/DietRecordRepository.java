package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.entities.DietRecordEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DietRecordRepository extends JpaRepository<DietRecordEntity, Long> {
    @Query("from DietRecordEntity dre where " +
            "dre.client.uuid = :clientUuid " +
            "and " +
            "dre.client.deleted = false and dre.client.deletedDate is null " +
            "and " +
            "dre.date = :date")
    List<DietRecordEntity> findByDate(@Param("clientUuid") String clientUuid,
                                      @Param("date") LocalDate date);

    @Query("from DietRecordEntity dre where " +
            "dre.client.uuid = :clientUuid " +
            "and " +
            "dre.client.deleted = false and dre.client.deletedDate is null " +
            "and " +
            "dre.date >= :startDate and dre.date <= :endDate")
    List<DietRecordEntity> findByDateRange(@Param("clientUuid") String clientUuid,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    @Query("from DietRecordEntity dre where dre.uuid = :uuid " +
            "and " +
            "dre.client.uuid = :clientUuid")
    Optional<DietRecordEntity> findByUuid(@Param("uuid") String uuid, @Param("clientUuid") String clientUuid);

    @Query("from DietRecordEntity dre where dre.uuid like concat('%', :uuid, '%') " +
            "and " +
            "dre.client.uuid = :clientUuid")
    List<DietRecordEntity> findByPartialUuid(@Param("uuid") String uuid, @Param("clientUuid") String clientUuid);

    @Modifying
    @Transactional
    @Query("delete from DietRecordEntity dre where dre.client.id = :id")
    void deleteAllForClientWithId(@Param("id") Long id);
}
