package ru.akvine.fitstats.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.SoftBaseEntity;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "CLIENT_ENTITY")
public class ClientEntity extends SoftBaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientEntitySeq")
    @SequenceGenerator(name = "clientEntitySeq", sequenceName = "SEQ_CLIENT_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "UUID", nullable = false)
    private String uuid;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "SECOND_NAME", nullable = false)
    private String secondName;

    @Column(name = "THIRD_NAME")
    private String thirdName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "HASH", nullable = false)
    private String hash;
}
