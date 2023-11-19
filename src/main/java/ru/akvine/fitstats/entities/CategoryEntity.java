package ru.akvine.fitstats.entities;


import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.SoftBaseEntity;
import ru.akvine.fitstats.enums.CategoryType;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "CATEGORY_ENTITY")
public class CategoryEntity extends SoftBaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoryEntitySeq")
    @SequenceGenerator(name = "categoryEntitySeq", sequenceName = "SEQ_CATEGORY_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType type;
}
