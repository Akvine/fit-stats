package ru.akvine.fitstats.repositories.profile;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.profile.ProfileChangeEmailActionEntity;
import ru.akvine.fitstats.repositories.security.ActionRepository;

public interface ProfileChangeEmailActionRepository extends ActionRepository<ProfileChangeEmailActionEntity> {
    @Query("from ProfileChangeEmailActionEntity  pceae where pceae.login = :login")
    ProfileChangeEmailActionEntity findCurrentAction(@Param("login") String login);
}
