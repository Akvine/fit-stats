package ru.akvine.fitstats.entities.profile;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.security.AccountPasswordable;
import ru.akvine.fitstats.entities.security.OneTimePasswordable;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.exceptions.security.NoMoreNewOtpAvailableException;

import javax.persistence.*;

@Entity
@Table(name = "PROFILE_CHANGE_EMAIL_ACTION_ENTITY")
@Data
@Accessors(chain = true)
public class ProfileChangeEmailActionEntity implements OneTimePasswordable, AccountPasswordable {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profileChangeEmailEntitySequence")
    @SequenceGenerator(name = "profileChangeEmailEntitySequence", sequenceName = "SEQ_PROFILE_CHANGE_EMAIL_ENTITY_ACTION", allocationSize = 1000)
    private Long id;

    @Column(name = "SESSION_ID", nullable = false)
    private String sessionId;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "NEW_EMAIL", nullable = false)
    private String newEmail;

    @Column(name = "PWD_INVALID_ATTEMPTS_LEFT", nullable = false)
    private int pwdInvalidAttemptsLeft;

    @Embedded
    private OtpActionEntity otpAction;

    @Transient
    public int decrementPwdInvalidAttemptsLeft() {
        if (this.pwdInvalidAttemptsLeft == 0) {
            throw new NoMoreNewOtpAvailableException("No more attempts to authenticate!");
        }
        return --this.pwdInvalidAttemptsLeft;
    }
}
