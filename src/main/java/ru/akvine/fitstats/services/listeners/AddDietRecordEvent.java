package ru.akvine.fitstats.services.listeners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.entities.DietSettingEntity;

import java.util.List;

@Getter
public class AddDietRecordEvent extends ApplicationEvent {
    private final Long clientId;
    private final DietSettingEntity dietSettingEntity;
    private final List<DietRecordEntity> records;

    public AddDietRecordEvent(Object source, Long clientId, DietSettingEntity dietSettingEntity, List<DietRecordEntity> records) {
        super(source);
        this.clientId = clientId;
        this.dietSettingEntity = dietSettingEntity;
        this.records = records;
    }
}
