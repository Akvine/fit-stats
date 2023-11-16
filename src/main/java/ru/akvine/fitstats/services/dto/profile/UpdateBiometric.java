package ru.akvine.fitstats.services.dto.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.PhysicalActivity;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateBiometric {
    private String clientUuid;
    @Nullable
    private Integer age;
    @Nullable
    private String weight;
    @Nullable
    private String height;
    @Nullable
    private PhysicalActivity physicalActivity;
}
