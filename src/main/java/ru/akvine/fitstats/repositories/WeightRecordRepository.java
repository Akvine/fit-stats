package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.WeightRecordEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface WeightRecordRepository extends JpaRepository<WeightRecordEntity, Long> {
    @Query("from WeightRecordEntity wre where wre.date = :date")
    Optional<WeightRecordEntity> findByDate(@Param("date") LocalDate date);
}
