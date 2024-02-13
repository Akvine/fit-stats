package ru.akvine.fitstats.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "DIET_RECORD_ENTITY")
public class DietRecordEntity extends BaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dietRecordEntitySeq")
    @SequenceGenerator(name = "dietRecordEntitySeq", sequenceName = "SEQ_DIET_RECORD_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "UUID", updatable = false, nullable = false)
    private String uuid;

    @JoinColumn(name = "CLIENT_ID", nullable = false)
    @ManyToOne
    private ClientEntity client;

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private ProductEntity product;

    @Column(name = "PROTEINS", nullable = false)
    private double proteins;

    @Column(name = "FATS", nullable = false)
    private double fats;

    @Column(name = "CARBOHYDRATES", nullable = false)
    private double carbohydrates;

    @Column(name = "CALORIES", nullable = false)
    private double calories;

    @Column(name = "ALCOHOL", nullable = false)
    private double alcohol;

    @Column(name = "VOLUME", nullable = false)
    private double volume;

    @Column(name = "DATE", nullable = false)
    private LocalDate date;

    @Column(name = "TIME")
    private LocalTime time;
}
