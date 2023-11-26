package ru.akvine.fitstats.repositories.profile;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.profile.ProfileChangePasswordActionEntity;
import ru.akvine.fitstats.repositories.security.ActionRepository;

public interface ProfileChangePasswordActionRepository extends ActionRepository<ProfileChangePasswordActionEntity> {
    @Query("from ProfileChangePasswordActionEntity pcpae where pcpae.login = :login")
    ProfileChangePasswordActionEntity findCurrentAction(@Param("login") String login);
}
