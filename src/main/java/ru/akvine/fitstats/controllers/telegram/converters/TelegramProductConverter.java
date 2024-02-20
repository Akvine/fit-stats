package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductAddRequest;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.utils.MathUtils;

import java.util.List;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Component
@RequiredArgsConstructor
public class TelegramProductConverter {
    private final static String NEXT_LINE = "\n";
    private final static int MAX_TELEGRAM_TEXT_LENGTH = 4096;
    private static final int DEFAULT_PRODUCT_VOLUME = 100;

    private static final double ALCOHOL_COEFFICIENT = 0.789;

    private final MessageResolveService messageResolveService;

    public SendMessage convertToProductListResponse(String chatId,
                                                    List<ProductBean> productBeans) {
        ClientSettingsBean clientSettingsBean = ClientSettingsContext.getClientSettingsContextHolder().getByThreadLocalForCurrent();
        Preconditions.checkNotNull(productBeans, "products is null");
        if (productBeans.isEmpty()) {
            return new SendMessage(
                    chatId,
                    messageResolveService.message(
                            MessageResolverCodes.PRODUCT_NOT_FOUND_CODE,
                            clientSettingsBean.getLanguage())
            );
        }
        String responseText = buildProductListResponse(productBeans);
        validateResponseText(responseText);
        return new SendMessage(
                chatId,
                responseText
        );
    }

    public ProductBean convertToAddProductStart(TelegramProductAddRequest request) {
        Preconditions.checkNotNull(request, "telegramProductAddRequest is null");
        return new ProductBean()
                .setTitle(request.getTitle())
                .setProducer(request.getProducer())
                .setFats(request.getFats())
                .setProteins(request.getProteins())
                .setVolume(DEFAULT_PRODUCT_VOLUME)
                .setCarbohydrates(request.getCarbohydrates())
                .setAlcohol(request.getVol() * ALCOHOL_COEFFICIENT)
                .setVol(request.getVol())
                .setMeasurement(VolumeMeasurement.safeValueOf(request.getVolumeMeasurement()))
                .setClientUuid(request.getClientUuid());
    }

    public SendMessage convertToProductAddResponse(String chatId,
                                                   ProductBean productBean) {
        return new SendMessage(
                chatId,
                buildProductAddResponse(productBean)
        );
    }

    private void validateResponseText(String text) {
        int length = text.length();
        if (length > MAX_TELEGRAM_TEXT_LENGTH) {
            String errorMessage = String
                    .format("Text is too long! Text length [%s] is greater than limit = [%s]", length, MAX_TELEGRAM_TEXT_LENGTH);
            throw new ValidationException(
                    CommonErrorCodes.Validation.Telegram.TEXT_MESSAGE_LENGTH_ERROR,
                    errorMessage);
        }
    }

    private String buildProductListResponse(List<ProductBean> products) {
        ClientSettingsBean clientSettingsBean = ClientSettingsContext.getClientSettingsContextHolder().getByThreadLocalForCurrent();
        int roundAccuracy = clientSettingsBean.getRoundAccuracy();
        Language language = clientSettingsBean.getLanguage();

        StringBuilder sb = new StringBuilder();
        sb.append("=======[")
                .append(messageResolveService.message(MessageResolverCodes.PRODUCT_LIST_CODE, language))
                .append("]=======").append(NEXT_LINE);

        int size = products.size();
        int lastElementIndex = products.size() - 1;
        for (int i = 0; i < size; ++i) {
            sb.append("--------------------").append(NEXT_LINE);
            sb.append("1. UUID: ").append(products.get(i).getUuid()).append(NEXT_LINE);
            sb.append("2. ")
                    .append(messageResolveService.message(MessageResolverCodes.PRODUCT_NAME_CODE, language))
                    .append(": ")
                    .append(products.get(i).getTitle()).append(NEXT_LINE);
            sb.append("3. ")
                    .append(messageResolveService.message(MessageResolverCodes.PRODUCER_NAME_CODE, language))
                    .append(": ")
                    .append(products.get(i).getProducer()).append(NEXT_LINE);
            sb.append("4. ")
                    .append(messageResolveService.message(MessageResolverCodes.PROTEINS_CODE, language))
                    .append(": ")
                    .append(round(products.get(i).getProteins(), roundAccuracy)).append(NEXT_LINE);
            sb.append("5. ")
                    .append(messageResolveService.message(MessageResolverCodes.FATS_CODE, language))
                    .append(": ")
                    .append(round(products.get(i).getFats(), roundAccuracy))
                    .append(NEXT_LINE);
            sb.append("6. ")
                    .append(messageResolveService.message(MessageResolverCodes.CARBOHYDRATES_CODE, language))
                    .append(": ")
                    .append(round(products.get(i).getCarbohydrates(), roundAccuracy))
                    .append(NEXT_LINE);
            sb.append("7. ")
                    .append(messageResolveService.message(MessageResolverCodes.ALCOHOL_CODE, language))
                    .append(": ")
                    .append(MathUtils.round(products.get(i).getAlcohol(), roundAccuracy))
                    .append(NEXT_LINE);
            sb.append("8. ")
                    .append(messageResolveService.message(MessageResolverCodes.CALORIES_CODE, language))
                    .append(round(products.get(i).getCalories(), roundAccuracy))
                    .append(NEXT_LINE);
            if (i == lastElementIndex) {
                sb.append("======================");
            }
        }

        return sb.toString();
    }

    private String buildProductAddResponse(ProductBean productBean) {
        ClientSettingsBean clientSettingsBean = ClientSettingsContext.getClientSettingsContextHolder().getByThreadLocalForCurrent();
        int roundAccuracy = clientSettingsBean.getRoundAccuracy();
        Language language = clientSettingsBean.getLanguage();

        StringBuilder sb = new StringBuilder();
        sb.append(messageResolveService.message(MessageResolverCodes.PRODUCT_ADDED_SUCCESSFUL_CODE, language))
                .append(": ")
                .append(NEXT_LINE);
        sb.append("1. UUID: ").append(productBean.getUuid()).append(NEXT_LINE);
        sb.append("2. ")
                .append(messageResolveService.message(MessageResolverCodes.PRODUCT_NAME_CODE, language))
                .append(": ")
                .append(productBean.getTitle()).append(NEXT_LINE);
        sb.append("3. ")
                .append(messageResolveService.message(MessageResolverCodes.PRODUCER_NAME_CODE, language))
                .append(": ")
                .append(productBean.getProducer()).append(NEXT_LINE);
        sb.append("4. ")
                .append(messageResolveService.message(MessageResolverCodes.PROTEINS_CODE, language))
                .append(": ")
                .append(round(productBean.getProteins(), roundAccuracy)).append(NEXT_LINE);
        sb.append("5. ")
                .append(messageResolveService.message(MessageResolverCodes.FATS_CODE, language))
                .append(": ")
                .append(round(productBean.getFats(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("6. ")
                .append(messageResolveService.message(MessageResolverCodes.CARBOHYDRATES_CODE, language))
                .append(": ")
                .append(round(productBean.getCarbohydrates(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("7. ")
                .append(messageResolveService.message(MessageResolverCodes.ALCOHOL_CODE, language))
                .append(": ")
                .append(round(productBean.getAlcohol(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("8. ")
                .append(messageResolveService.message(MessageResolverCodes.CALORIES_CODE, language))
                .append(round(productBean.getCalories(), roundAccuracy))
                .append(NEXT_LINE);
        sb.append("======================");

        return sb.toString();
    }
}
