package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.converters.TelegramProductConverter;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductAddRequest;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductListRequest;
import ru.akvine.fitstats.controllers.telegram.validators.TelegramProductValidator;
import ru.akvine.fitstats.services.ProductService;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.ProductBean;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramProductResolver {
    private final ProductService productService;
    private final TelegramProductConverter telegramProductConverter;
    private final TelegramProductValidator telegramProductValidator;

    public SendMessage add(TelegramProductAddRequest telegramProductAddRequest) {
        telegramProductValidator.verifyTelegramProductAddRequest(telegramProductAddRequest);
        ProductBean addProductStart = telegramProductConverter.convertToAddProductStart(telegramProductAddRequest);
        ProductBean addProductFinish = productService.add(addProductStart);
        return telegramProductConverter.convertToProductAddResponse(
                telegramProductAddRequest.getChatId(),
                addProductFinish);
    }

    public SendMessage list(TelegramProductListRequest telegramProductListRequest) {
        List<ProductBean> products = productService.findByFilter(new Filter().setFilterName(telegramProductListRequest.getFilter()));
        return telegramProductConverter.convertToProductListResponse(telegramProductListRequest.getChatId(), products);
    }
}
