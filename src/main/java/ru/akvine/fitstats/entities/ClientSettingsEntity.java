package ru.akvine.fitstats.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.base.BaseEntity;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;

import javax.persistence.*;

@Entity
@Table(name = "CLIENT_SETTINGS_ENTITY")
@Getter
@Setter
@Accessors(chain = true)
public class ClientSettingsEntity extends BaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientSettingsEntitySeq")
    @SequenceGenerator(name = "clientSettingsEntitySeq", sequenceName = "SEQ_CLIENT_SETTINGS_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "LANGUAGE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name = "ROUND_ACCURACY", nullable = false)
    private int roundAccuracy;

    @Column(name = "TELEGRAM_PRINT_STATISTIC_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PrintMacronutrientsMode printMacronutrientsMode;

    @OneToOne
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private ClientEntity clientEntity;
}
