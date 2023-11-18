package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query("from ProductEntity pe where pe.deleted = false and pe.deletedDate is null " +
            "and " +
            "(lower(pe.title) like concat('%', :filter, '%') or lower(pe.producer) like concat('%', :filter, '%'))")
    List<ProductEntity> findByFilter(@Param("filter") String filter);

    @Query("from ProductEntity pe where pe.deleted = false and pe.deletedDate is null " +
            "and " +
            "pe.uuid = :uuid")
    Optional<ProductEntity> findByUuidAndNotDeleted(@Param("uuid") String uuid);

    @Query("from ProductEntity pe where pe.uuid = :uuid")
    Optional<ProductEntity> findByUuid(@Param("uuid") String uuid);

    @Query("from ProductEntity pe where pe.deleted = false and pe.deletedDate is null")
    List<ProductEntity> findAll();
}
