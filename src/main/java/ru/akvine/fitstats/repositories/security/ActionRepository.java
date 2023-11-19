package ru.akvine.fitstats.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.akvine.fitstats.entities.security.OneTimePasswordable;

@NoRepositoryBean
public interface ActionRepository <T extends OneTimePasswordable> extends JpaRepository<T, Long> {
    T findCurrentAction(String payload);
}
