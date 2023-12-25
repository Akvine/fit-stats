package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductAddRequest;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.utils.MathUtils;

import java.util.List;

@Component
public class TelegramProductConverter {
    private final static String NEXT_LINE = "\n";
    private final static int MAX_TELEGRAM_TEXT_LENGTH = 4096;
    private static final int DEFAULT_PRODUCT_VOLUME = 100;

    public SendMessage convertToProductListResponse(String chatId, List<ProductBean> productBeans) {
        Preconditions.checkNotNull(productBeans, "products is null");
        if (productBeans.isEmpty()) {
            return new SendMessage(
                    chatId,
                    "Упс! Ничего не нашли :( Попробуйте ввести другой фильтр"
            );
        }
        String responseText = buildProductListResponse(productBeans);
        validateResponseText(responseText);
        return new SendMessage(
                chatId,
                responseText
        );
    }

    public ProductBean convertToProductBean(TelegramProductAddRequest request) {
        Preconditions.checkNotNull(request, "telegramProductAddRequest is null");
        return new ProductBean()
                .setTitle(request.getTitle())
                .setProducer(request.getProducer())
                .setFats(request.getFats())
                .setProteins(request.getProteins())
                .setVolume(DEFAULT_PRODUCT_VOLUME)
                .setCarbohydrates(request.getCarbohydrates())
                .setVol(request.getVol())
                .setMeasurement(VolumeMeasurement.safeValueOf(request.getVolumeMeasurement()));
    }

    public SendMessage convertToProductAddResponse(String chatId, ProductBean productBean) {
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
        int roundAccuracy = 2;

        StringBuilder sb = new StringBuilder();
        sb.append("=======[Список продуктов]=======").append(NEXT_LINE);

        int size = products.size();
        int lastElementIndex = products.size() - 1;
        for (int i = 0; i < size; ++i) {
            sb.append("--------------------").append(NEXT_LINE);
            sb.append("1. UUID: ").append(products.get(i).getUuid()).append(NEXT_LINE);
            sb.append("2. Название: ").append(products.get(i).getTitle()).append(NEXT_LINE);
            sb.append("3. Производитель: ").append(products.get(i).getProducer()).append(NEXT_LINE);
            sb.append("4. Белка: ").append(MathUtils.round(products.get(i).getProteins(), roundAccuracy)).append(NEXT_LINE);
            sb.append("5. Жиров: ").append(MathUtils.round(products.get(i).getFats(), roundAccuracy)).append(NEXT_LINE);
            sb.append("6. Углеводов: ").append(MathUtils.round(products.get(i).getCarbohydrates(), roundAccuracy)).append(NEXT_LINE);
            sb.append("7. Калории: ").append(MathUtils.round(products.get(i).getCalories(), roundAccuracy)).append(NEXT_LINE);
            if (i == lastElementIndex) {
                sb.append("======================");
            }
        }

        return sb.toString();
    }

    private String buildProductAddResponse(ProductBean productBean) {
        int roundAccuracy = 2;
        StringBuilder sb = new StringBuilder();
        sb.append("Продукт был успешно добавлен: ").append(NEXT_LINE);
        sb.append("1. UUID: ").append(productBean.getUuid()).append(NEXT_LINE);
        sb.append("2. Название: ").append(productBean.getTitle()).append(NEXT_LINE);
        sb.append("3. Производитель: ").append(productBean.getProducer()).append(NEXT_LINE);
        sb.append("4. Белка: ").append(MathUtils.round(productBean.getProteins(), roundAccuracy)).append(NEXT_LINE);
        sb.append("5. Жиры: ").append(MathUtils.round(productBean.getFats(), roundAccuracy)).append(NEXT_LINE);
        sb.append("6. Углеводы: ").append(MathUtils.round(productBean.getCarbohydrates(), roundAccuracy)).append(NEXT_LINE);
        sb.append("7. Калории: ").append(MathUtils.round(productBean.getCalories(), roundAccuracy)).append(NEXT_LINE);
        sb.append("======================");

        return sb.toString();
    }
}
