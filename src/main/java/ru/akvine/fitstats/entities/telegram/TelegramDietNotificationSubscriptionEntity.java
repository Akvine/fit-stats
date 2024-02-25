package ru.akvine.fitstats.entities.telegram;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.enums.telegram.DietNotificationSubscriptionType;

import javax.persistence.*;

@Entity
@Table(name = "TELEGRAM_DIET_NOTIFICATION_SUBSCRIPTION_ENTITY")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class TelegramDietNotificationSubscriptionEntity extends TelegramNotificationSubscriptionEntity {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telegramDietNotificationSubscriptionEntitySeq")
    @SequenceGenerator(name = "telegramDietNotificationSubscriptionEntitySeq", sequenceName = "SEQ_TELEGRAM_DIET_NOTIFICATION_SUBSCRIPTION_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "DIET_NOTIFICATION_SUBSCRIPTION_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private DietNotificationSubscriptionType dietNotificationSubscriptionType;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private ClientEntity client;

    @Column(name = "IS_PROCESSED", nullable = false)
    private boolean processed;
}
