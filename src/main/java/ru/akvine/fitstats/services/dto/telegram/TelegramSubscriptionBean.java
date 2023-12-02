package ru.akvine.fitstats.services.dto.telegram;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.telegram.TelegramSubscriptionEntity;
import ru.akvine.fitstats.services.dto.base.SoftBean;
import ru.akvine.fitstats.services.dto.client.ClientBean;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TelegramSubscriptionBean extends SoftBean {
    private Long id;
    private Long telegramId;
    private ClientBean client;
    private LocalDateTime createdDate;

    public TelegramSubscriptionBean(TelegramSubscriptionEntity telegramSubscriptionEntity) {
        this.id = telegramSubscriptionEntity.getId();
        this.telegramId = telegramSubscriptionEntity.getTelegramId();
        this.client = new ClientBean(telegramSubscriptionEntity.getClient());

        this.createdDate = telegramSubscriptionEntity.getCreatedDate();
        this.updatedDate = telegramSubscriptionEntity.getUpdatedDate();
        this.deletedDate = telegramSubscriptionEntity.getDeletedDate();
        this.deleted = telegramSubscriptionEntity.isDeleted();
    }
}
