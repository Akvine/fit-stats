package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.akvine.fitstats.entities.BiometricEntity;

public interface BiometricRepository extends JpaRepository<BiometricEntity, Long> {
}
