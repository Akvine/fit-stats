package ru.akvine.fitstats.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.SoftBaseEntity;
import ru.akvine.fitstats.enums.BarCodeType;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "BARCODE_ENTITY")
public class BarCodeEntity extends SoftBaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "barcodeEntitySeq")
    @SequenceGenerator(name = "barcodeEntitySeq", sequenceName = "SEQ_BARCODE_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "NUMBER", nullable = false)
    private String number;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private BarCodeType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private ProductEntity product;
}
