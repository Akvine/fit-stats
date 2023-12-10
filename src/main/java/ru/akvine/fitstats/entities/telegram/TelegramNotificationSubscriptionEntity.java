package ru.akvine.fitstats.entities.telegram;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.akvine.fitstats.entities.ClientEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class TelegramNotificationSubscriptionEntity {
    @Column(name = "TELEGRAM_ID", nullable = false)
    private Long telegramId;

    @Column(name = "IS_PROCESSED", nullable = false)
    private boolean processed;
}
