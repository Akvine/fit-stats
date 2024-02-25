package ru.akvine.fitstats.entities.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.security.AccountPasswordable;
import ru.akvine.fitstats.entities.security.OneTimePasswordable;
import ru.akvine.fitstats.entities.security.OtpActionEntity;
import ru.akvine.fitstats.exceptions.security.NoMoreOtpInvalidAttemptsException;

import javax.persistence.*;

@Entity
@Table(name = "PROFILE_CHANGE_PASSWORD_ACTION_ENTITY")
@Getter
@Setter
@Accessors(chain = true)
public class ProfileChangePasswordActionEntity implements OneTimePasswordable, AccountPasswordable {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profileChangePasswordEntitySequence")
    @SequenceGenerator(name = "profileChangePasswordEntitySequence", sequenceName = "SEQ_PROFILE_CHANGE_PASSWORD_ACTION_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "SESSION_ID", nullable = false)
    private String sessionId;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "NEW_HASH")
    private String newHash;

    @Column(name = "PWD_INVALID_ATTEMPTS_LEFT", nullable = false)
    private int pwdInvalidAttemptsLeft;

    @Embedded
    private OtpActionEntity otpAction;

    @Transient
    public int decrementPwdInvalidAttemptsLeft() {
        if (this.pwdInvalidAttemptsLeft == 0) {
            throw new NoMoreOtpInvalidAttemptsException("No more attempts to authenticate!");
        }
        return --this.pwdInvalidAttemptsLeft;
    }
}
