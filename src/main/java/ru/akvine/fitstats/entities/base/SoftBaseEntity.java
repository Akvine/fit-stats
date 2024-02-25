package ru.akvine.fitstats.entities.base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
public abstract class SoftBaseEntity extends BaseEntity {
    @Column(name = "DELETED_DATE")
    private LocalDateTime deletedDate;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted;
}
