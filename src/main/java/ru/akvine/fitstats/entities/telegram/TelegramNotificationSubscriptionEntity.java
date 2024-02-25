package ru.akvine.fitstats.entities.telegram;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class TelegramNotificationSubscriptionEntity {
    @Column(name = "TELEGRAM_ID", nullable = false)
    private Long telegramId;
}
