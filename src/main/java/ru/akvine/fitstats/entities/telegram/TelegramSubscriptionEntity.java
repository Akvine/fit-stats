package ru.akvine.fitstats.entities.telegram;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.base.SoftBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "TELEGRAM_SUBSCRIPTION_ENTITY")
@Data
@Accessors(chain = true)
public class TelegramSubscriptionEntity extends SoftBaseEntity {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telegramSubscriptionEntitySeq")
    @SequenceGenerator(name = "telegramSubscriptionEntitySeq", sequenceName = "SEQ_TELEGRAM_SUBSCRIPTION_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "TELEGRAM_ID", nullable = false)
    private Long telegramId;

    @Column(name = "CHAT_ID", nullable = false)
    private String chatId;

    @OneToOne
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private TelegramSubscriptionTypeEntity type;

    @OneToOne
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private ClientEntity client;
}
