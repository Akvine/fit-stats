package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.DietSettingEntity;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.services.dto.base.SoftBean;
import ru.akvine.fitstats.services.dto.client.ClientBean;

@Data
@Accessors(chain = true)
public class DietSettingBean extends SoftBean {
    private Long id;
    private ClientBean clientBean;
    private double maxProteins;
    private double maxFats;
    private double maxCarbohydrates;
    private double maxCalories;
    private Diet diet;

    public DietSettingBean(DietSettingEntity dietSettingEntity) {
        this.id = dietSettingEntity.getId();
        this.clientBean = new ClientBean(dietSettingEntity.getClient());
        this.maxProteins = dietSettingEntity.getMaxProteins();
        this.maxFats = dietSettingEntity.getMaxFats();
        this.maxCarbohydrates = dietSettingEntity.getMaxCarbohydrates();
        this.maxCalories = dietSettingEntity.getMaxCalories();
        this.diet = dietSettingEntity.getDiet();

        this.createdDate = dietSettingEntity.getCreatedDate();
        this.updatedDate = dietSettingEntity.getUpdatedDate();
        this.deletedDate = dietSettingEntity.getDeletedDate();
        this.deleted = dietSettingEntity.isDeleted();
    }
}
