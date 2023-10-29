package ru.akvine.fitstats.services.dto.base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public abstract class Bean {
    protected LocalDateTime createdDate;
    @Nullable
    protected LocalDateTime updatedDate;
}
