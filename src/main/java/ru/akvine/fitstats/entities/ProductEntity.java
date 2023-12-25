package ru.akvine.fitstats.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.SoftBaseEntity;
import ru.akvine.fitstats.enums.VolumeMeasurement;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "PRODUCT_ENTITY")
public class ProductEntity extends SoftBaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productEntitySeq")
    @SequenceGenerator(name = "productEntitySeq", sequenceName = "SEQ_PRODUCT_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "UUID", nullable = false)
    private String uuid;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "PRODUCER", nullable = false)
    private String producer;

    @Column(name = "PROTEINS", nullable = false)
    private double proteins;

    @Column(name = "FATS", nullable = false)
    private double fats;

    @Column(name = "CARBOHYDRATES", nullable = false)
    private double carbohydrates;

    @Column(name = "CALORIES", nullable = false)
    private double calories;

    @Column(name = "VOL", nullable = false)
    private double vol;

    @Column(name = "VOLUME", nullable = false)
    private double volume;

    @Column(name = "MEASUREMENT")
    @Enumerated(EnumType.STRING)
    private VolumeMeasurement measurement;
}
