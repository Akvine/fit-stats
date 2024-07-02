package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.ClientEntity;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    @Query("from ClientEntity ce where ce.email = :email " +
            "and " +
            "ce.deleted = false")
    Optional<ClientEntity> findByEmail(@Param("email") String email);

    @Query("from ClientEntity ce where ce.uuid = :uuid " +
            "and " +
            "ce.deleted = false")
    Optional<ClientEntity> findByUuid(@Param("uuid") String uuid);
}
