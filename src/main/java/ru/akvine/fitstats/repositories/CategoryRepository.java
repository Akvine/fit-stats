package ru.akvine.fitstats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.fitstats.entities.CategoryEntity;

import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("from CategoryEntity ce where ce.deletedDate is null and ce.deleted = false and ce.title = :title")
    Optional<CategoryEntity> findByTitleAndNotDeleted(@Param("title") String title);

    @Query("from CategoryEntity ce where ce.deletedDate is null and ce.deleted = false and ce.title in :titles")
    Set<CategoryEntity> findByTitles(@Param("titles") Set<String> titles);
}
