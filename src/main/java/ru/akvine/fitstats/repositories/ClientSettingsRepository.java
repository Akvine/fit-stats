package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.ClientSettingsEntity;

import java.util.Optional;

public interface ClientSettingsRepository extends JpaRepository<ClientSettingsEntity, Long> {
    @Query("from ClientSettingsEntity cse where " +
            "cse.clientEntity.deleted = false and cse.clientEntity.deletedDate is null " +
            "and cse.clientEntity.email = :email")
    Optional<ClientSettingsEntity> findByEmail(@Param("email") String email);
}
