package ru.akvine.fitstats.services.dto.category;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.CategoryEntity;
import ru.akvine.fitstats.enums.CategoryType;
import ru.akvine.fitstats.services.dto.base.SoftBean;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CategoryBean extends SoftBean {
    private Long id;
    private String title;
    private CategoryType type;

    public CategoryBean(CategoryEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.type = entity.getType();

        this.createdDate = entity.getCreatedDate();
        this.updatedDate = entity.getUpdatedDate();
        this.deleted = entity.isDeleted();
        this.deletedDate = entity.getDeletedDate();
    }
}
