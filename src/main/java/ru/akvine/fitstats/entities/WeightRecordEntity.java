package ru.akvine.fitstats.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "WEIGHT_RECORD_ENTITY")
@Data
@Accessors(chain = true)
public class WeightRecordEntity extends BaseEntity  {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weightRecordEntitySeq")
    @SequenceGenerator(name = "weightRecordEntitySeq", sequenceName = "SEQ_WEIGHT_RECORD_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "DATE", nullable = false)
    private LocalDate date;

    @JoinColumn(name = "CLIENT_ID", nullable = false)
    @ManyToOne
    private ClientEntity client;

    @Column(name = "WEIGHT_VALUE", nullable = false)
    private String value;
}
