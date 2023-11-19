package ru.akvine.fitstats.entities.security;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.RegistrationState;

import javax.persistence.*;

@Entity
@Table(name = "REGISTRATION_ACTION_ENTITY")
@Data
@Accessors(chain = true)
public class RegistrationActionEntity implements OneTimePasswordable {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registrationActionEntitySequence")
    @SequenceGenerator(name = "registrationActionEntitySequence", sequenceName = "SEQ_REGISTRATION_ACTION_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "SESSION_ID", nullable = false)
    private String sessionId;

    @Column(name = "STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationState state = RegistrationState.NEW;

    @Embedded
    private OtpActionEntity otpAction;
}
