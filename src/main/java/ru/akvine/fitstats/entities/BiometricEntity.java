package ru.akvine.fitstats.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.BaseEntity;
import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.enums.HeightMeasurement;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.enums.WeightMeasurement;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "BIOMETRIC_ENTITY")
public class BiometricEntity extends BaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "biometricEntitySeq")
    @SequenceGenerator(name = "biometricEntitySeq", sequenceName = "SEQ_BIOMETRIC_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "AGE", nullable = false)
    private int age;

    @Column(name = "WEIGHT", nullable = false)
    private String weight;

    @Column(name = "HEIGHT", nullable = false)
    private String height;

    @Column(name = "PHYSICAL_ACTIVITY", nullable = false)
    @Enumerated(EnumType.STRING)
    private PhysicalActivity physicalActivity;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "WEIGHT_MEASUREMENT", nullable = false)
    @Enumerated(EnumType.STRING)
    private WeightMeasurement weightMeasurement;

    @Column(name = "HEIGHT_MEASUREMENT", nullable = false)
    @Enumerated(EnumType.STRING)
    private HeightMeasurement heightMeasurement;

    @OneToOne
    @JoinColumn(name = "CLIENT_ID")
    private ClientEntity clientEntity;
}
