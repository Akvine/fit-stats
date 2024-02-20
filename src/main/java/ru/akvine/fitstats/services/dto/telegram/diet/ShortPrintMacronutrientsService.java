package ru.akvine.fitstats.services.dto.telegram.diet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.dto.diet.DietDisplay;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Service
@RequiredArgsConstructor
public class ShortPrintMacronutrientsService implements PrintMacronutrientsService {
    private static final String FORWARD_SLASH = " / ";

    private final MessageResolveService messageResolveService;

    @Override
    public String print(DietDisplay dietDisplay, ClientSettingsBean clientSettingsBean) {
        return buildStatistic(dietDisplay, clientSettingsBean);
    }

    @Override
    public PrintMacronutrientsMode getType() {
        return PrintMacronutrientsMode.SHORT;
    }

    private String buildStatistic(DietDisplay dietDisplay, ClientSettingsBean clientSettingsBean) {
        int roundAccuracy = clientSettingsBean.getRoundAccuracy();
        Language language = clientSettingsBean.getLanguage();

        StringBuilder sb = new StringBuilder();

        sb.append("1. ")
                .append(messageResolveService.message(MessageResolverCodes.CALORIES_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getCurrentCalories(), roundAccuracy))
                .append(FORWARD_SLASH)
                .append(round(dietDisplay.getMaxCalories(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("2. ")
                .append(messageResolveService.message(MessageResolverCodes.PROTEINS_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getCurrentProteins(), roundAccuracy))
                .append(FORWARD_SLASH)
                .append(round(dietDisplay.getMaxProteins(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("3. ")
                .append(messageResolveService.message(MessageResolverCodes.FATS_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getCurrentFats(), roundAccuracy))
                .append(FORWARD_SLASH)
                .append(round(dietDisplay.getMaxFats(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("4. ")
                .append(messageResolveService.message(MessageResolverCodes.CARBOHYDRATES_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getCurrentCarbohydrates(), roundAccuracy))
                .append(FORWARD_SLASH)
                .append(round(dietDisplay.getMaxCarbohydrates(), roundAccuracy))
                .append(NEXT_LINE);

        return sb.toString();
    }
}
