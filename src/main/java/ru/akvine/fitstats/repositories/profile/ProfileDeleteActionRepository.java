package ru.akvine.fitstats.repositories.profile;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.profile.ProfileDeleteActionEntity;
import ru.akvine.fitstats.repositories.security.ActionRepository;

public interface ProfileDeleteActionRepository extends ActionRepository<ProfileDeleteActionEntity> {
    @Query("from ProfileDeleteActionEntity pdae where pdae.login = :login")
    ProfileDeleteActionEntity findCurrentAction(@Param("login") String login);
}
