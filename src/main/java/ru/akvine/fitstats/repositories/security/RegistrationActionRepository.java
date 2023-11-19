package ru.akvine.fitstats.repositories.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.security.RegistrationActionEntity;

public interface RegistrationActionRepository extends ActionRepository<RegistrationActionEntity> {
    @Query("from RegistrationActionEntity rae where rae.login = :login")
    RegistrationActionEntity findCurrentAction(@Param("login") String login);
}
