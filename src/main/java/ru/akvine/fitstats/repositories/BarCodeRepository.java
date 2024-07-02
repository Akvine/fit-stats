package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.BarCodeEntity;

import java.util.List;
import java.util.Optional;

public interface BarCodeRepository extends JpaRepository<BarCodeEntity, Long> {
    @Query("from BarCodeEntity bce " +
            "where bce.product.uuid = :uuid " +
            "and bce.product.deleted = false " +
            "and bce.deleted = false")
    List<BarCodeEntity> findByProductUuid(@Param("uuid") String uuid);

    @Query("from BarCodeEntity bce " +
            "where bce.number = :number " +
            "and bce.deleted = false and bce.deletedDate is null")
    Optional<BarCodeEntity> findByNumber(@Param("number") String number);
}
