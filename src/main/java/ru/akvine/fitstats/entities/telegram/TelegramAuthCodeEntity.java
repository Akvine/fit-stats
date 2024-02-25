package ru.akvine.fitstats.entities.telegram;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.ClientEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TELEGRAM_AUTH_CODE_ENTITY")
@Getter
@Setter
@Accessors(chain = true)
@ToString(exclude = {"client", "code", "expiredAt"})
@EqualsAndHashCode(exclude = {"client"})
public class TelegramAuthCodeEntity {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telegramAuthCodeEntitySequence")
    @SequenceGenerator(name = "telegramAuthCodeEntitySequence", sequenceName = "SEQ_TELEGRAM_AUTH_CODE_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "EXPIRED_AT", nullable = false)
    private LocalDateTime expiredAt;

    @OneToOne
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private ClientEntity client;

    @Transient
    public boolean isExpired() {
        LocalDateTime expiredAt = this.expiredAt;
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
