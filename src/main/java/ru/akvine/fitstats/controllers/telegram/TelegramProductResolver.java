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
        ProductBean productBean = telegramProductConverter.convertToProductBean(telegramProductAddRequest);
        ProductBean savedProductBean = productService.add(productBean);
        return telegramProductConverter.convertToProductAddResponse(telegramProductAddRequest.getChatId(), savedProductBean);
    }

    public SendMessage list(TelegramProductListRequest telegramProductListRequest) {
        List<ProductBean> products = productService.findByFilter(new Filter().setFilterName(telegramProductListRequest.getFilter()));
        return telegramProductConverter.convertToProductListResponse(telegramProductListRequest.getChatId(), products);
    }
}
