package ru.akvine.fitstats.controllers.telegram.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.telegram.dto.statistic.TelegramStatisticHistoryRequest;
import ru.akvine.fitstats.validators.DurationValidator;
import ru.akvine.fitstats.validators.MacronutrientValidator;

@Component
@RequiredArgsConstructor
public class TelegramStatisticValidator {
    private final DurationValidator durationValidator;
    private final MacronutrientValidator macronutrientValidator;

    public void verifyTelegramStatisticHistoryRequest(TelegramStatisticHistoryRequest request) {
        Preconditions.checkNotNull(request, "telegramStatisticHistoryRequest is null");
        durationValidator.validate(request.getDuration());
        macronutrientValidator.validate(request.getMacronutrient());
    }
}
