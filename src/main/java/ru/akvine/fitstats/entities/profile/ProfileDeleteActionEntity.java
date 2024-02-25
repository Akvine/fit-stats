package ru.akvine.fitstats.entities.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.security.AccountPasswordable;
import ru.akvine.fitstats.entities.security.OneTimePasswordable;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.exceptions.security.NoMoreNewOtpAvailableException;

import javax.persistence.*;

@Entity
@Table(name = "PROFILE_DELETE_ACTION_ENTITY")
@Getter
@Setter
@Accessors(chain = true)
public class ProfileDeleteActionEntity implements OneTimePasswordable, AccountPasswordable {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profileDeleteEntitySequence")
    @SequenceGenerator(name = "profileDeleteEntitySequence", sequenceName = "SEQ_PROFILE_DELETE_ACTION_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "SESSION_ID", nullable = false)
    private String sessionId;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "PWD_INVALID_ATTEMPTS_LEFT", nullable = false)
    private int pwdInvalidAttemptsLeft;

    @Embedded
    private OtpActionEntity otpAction;

    @Transient
    public int decrementPwdInvalidAttemptsLeft() {
        if (this.pwdInvalidAttemptsLeft <= 0) {
            throw new NoMoreNewOtpAvailableException("No more attempts to authenticate!");
        }
        return --this.pwdInvalidAttemptsLeft;
    }
}
