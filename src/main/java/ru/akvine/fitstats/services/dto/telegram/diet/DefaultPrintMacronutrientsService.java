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
public class DefaultPrintMacronutrientsService implements PrintMacronutrientsService {
    private final MessageResolveService messageResolveService;


    @Override
    public String print(DietDisplay dietDisplay, ClientSettingsBean clientSettingsBean) {
        return buildStatistic(dietDisplay, clientSettingsBean);
    }

    @Override
    public PrintMacronutrientsMode getType() {
        return PrintMacronutrientsMode.DEFAULT;
    }

    private String buildStatistic(DietDisplay dietDisplay, ClientSettingsBean clientSettingsBean) {
        StringBuilder sb = new StringBuilder();
        sb = buildRemained(sb, dietDisplay, clientSettingsBean);
        sb = buildConsumed(sb, dietDisplay, clientSettingsBean);
        sb = buildMax(sb, dietDisplay, clientSettingsBean);

        return sb.toString();
    }

    private StringBuilder buildRemained(StringBuilder sb,
                                        DietDisplay dietDisplay,
                                        ClientSettingsBean clientSettingsBean) {
        int roundAccuracy = clientSettingsBean.getRoundAccuracy();
        Language language = clientSettingsBean.getLanguage();

        sb.append("=======[");
        sb.append(messageResolveService.message(MessageResolverCodes.REMAINED_CODE, language));
        sb.append("]=======").append(NEXT_LINE);

        sb.append("1. ")
                .append(messageResolveService.message(MessageResolverCodes.REMAINED_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getRemainingCalories(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("2. ")
                .append(messageResolveService.message(MessageResolverCodes.PROTEINS_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getRemainingProteins(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("3. ")
                .append(messageResolveService.message(MessageResolverCodes.FATS_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getRemainingFats(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("4. ")
                .append(messageResolveService.message(MessageResolverCodes.CARBOHYDRATES_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getRemainingCarbohydrates(), roundAccuracy))
                .append(NEXT_LINE);
        return sb;
    }

    private StringBuilder buildConsumed(StringBuilder sb,
                                        DietDisplay dietDisplay,
                                        ClientSettingsBean clientSettingsBean) {
        int roundAccuracy = clientSettingsBean.getRoundAccuracy();
        Language language = clientSettingsBean.getLanguage();

        sb.append("=======[");
        sb.append(messageResolveService.message(MessageResolverCodes.CONSUMED_CODE, language));
        sb.append("]=======").append(NEXT_LINE);

        sb.append("1. ")
                .append(messageResolveService.message(MessageResolverCodes.CALORIES_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getCurrentCalories(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("2. ")
                .append(messageResolveService.message(MessageResolverCodes.PROTEINS_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getCurrentProteins(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("3. ")
                .append(messageResolveService.message(MessageResolverCodes.FATS_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getCurrentFats(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("4. ")
                .append(messageResolveService.message(MessageResolverCodes.CARBOHYDRATES_CODE, language))
                .append(round(dietDisplay.getCurrentCarbohydrates(), roundAccuracy))
                .append(NEXT_LINE);
        return sb;
    }

    private StringBuilder buildMax(StringBuilder sb,
                                   DietDisplay dietDisplay,
                                   ClientSettingsBean clientSettingsBean) {
        int roundAccuracy = clientSettingsBean.getRoundAccuracy();
        Language language = clientSettingsBean.getLanguage();

        sb.append("=======[");
        sb.append(messageResolveService.message(MessageResolverCodes.MAXIMUM_CODE, language));
        sb.append("]=======").append(NEXT_LINE);

        sb.append("1. ")
                .append(messageResolveService.message(MessageResolverCodes.CALORIES_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getMaxCalories(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("2. ")
                .append(messageResolveService.message(MessageResolverCodes.PROTEINS_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getMaxProteins(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("3. ")
                .append(messageResolveService.message(MessageResolverCodes.FATS_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getMaxFats(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("4. ")
                .append(messageResolveService.message(MessageResolverCodes.CARBOHYDRATES_CODE, language))
                .append(": ")
                .append(round(dietDisplay.getMaxCarbohydrates(), roundAccuracy))
                .append(NEXT_LINE);

        sb.append("======================");
        return sb;
    }
}
