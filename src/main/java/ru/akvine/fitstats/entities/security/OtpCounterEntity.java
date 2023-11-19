package ru.akvine.fitstats.entities.security;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OTP_COUNTER_ENTITY")
@Data
@Accessors(chain = true)
public class OtpCounterEntity {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otpCounterEntitySequence")
    @SequenceGenerator(name = "otpCounterEntitySequence", sequenceName = "SEQ_OTP_COUNTER_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "VALUE", nullable = false)
    private long value = 1L;

    @Column(name = "LAST_UPDATE", nullable = false)
    private LocalDateTime lastUpdated;
}
