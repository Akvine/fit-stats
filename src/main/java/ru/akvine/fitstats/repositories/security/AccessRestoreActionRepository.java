package ru.akvine.fitstats.repositories.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.security.AccessRestoreActionEntity;

public interface AccessRestoreActionRepository extends ActionRepository<AccessRestoreActionEntity> {
    @Query("from AccessRestoreActionEntity arae where arae.login = :login")
    AccessRestoreActionEntity findCurrentAction(@Param("login") String login);
}
