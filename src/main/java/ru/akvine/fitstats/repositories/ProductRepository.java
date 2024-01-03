package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    @Query("from ProductEntity pe where pe.deleted = false and pe.deletedDate is null " +
            "and " +
            "pe.uuid = :uuid")
    Optional<ProductEntity> findByUuidAndNotDeleted(@Param("uuid") String uuid);

    @Query("from ProductEntity pe where pe.deleted = false and pe.deletedDate is null " +
            "and " +
            "pe.uuid like concat('%', :uuid, '%')")
    List<ProductEntity> findByPartialUuidAndNotDeleted(@Param("uuid") String uuid);

    @Query("from ProductEntity pe where pe.uuid = :uuid")
    Optional<ProductEntity> findByUuid(@Param("uuid") String uuid);

    @Query("from ProductEntity pe where pe.deleted = false and pe.deletedDate is null")
    List<ProductEntity> findAll();
}
