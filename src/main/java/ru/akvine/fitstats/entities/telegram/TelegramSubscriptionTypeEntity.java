package ru.akvine.fitstats.entities.telegram;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "TELEGRAM_SUBSCRIPTION_TYPE_ENTITY")
@Getter
@Setter
@Accessors(chain = true)
public class TelegramSubscriptionTypeEntity extends BaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telegramSubscriptionTypeSeq")
    @SequenceGenerator(name = "telegramSubscriptionTypeSeq", sequenceName = "SEQ_TELEGRAM_SUBSCRIPTION_TYPE_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "CODE", nullable = false)
    private String code;
}
