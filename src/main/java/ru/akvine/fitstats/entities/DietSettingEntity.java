package ru.akvine.fitstats.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.SoftBaseEntity;
import ru.akvine.fitstats.enums.Diet;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "DIET_SETTING_ENTITY")
public class DietSettingEntity extends SoftBaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dietSettingEntitySeq")
    @SequenceGenerator(name = "dietSettingEntitySeq", sequenceName = "SEQ_DIET_SETTING_ENTITY", allocationSize = 1000)
    private Long id;

    @JoinColumn(name = "CLIENT_ID")
    @OneToOne
    private ClientEntity client;

    @Column(name = "MAX_PROTEINS", nullable = false)
    private double maxProteins;

    @Column(name = "MAX_FATS", nullable = false)
    private double maxFats;

    @Column(name = "MAX_CARBOHYDRATES", nullable = false)
    private double maxCarbohydrates;

    @Column(name = "MAX_CALORIES", nullable = false)
    private double maxCalories;

    @Column(name = "DIET_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Diet diet;
}
