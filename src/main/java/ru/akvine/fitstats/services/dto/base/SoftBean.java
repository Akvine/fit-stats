package ru.akvine.fitstats.services.dto.base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public abstract class SoftBean extends Bean {
    protected LocalDateTime deletedDate;
    protected boolean deleted;
}
