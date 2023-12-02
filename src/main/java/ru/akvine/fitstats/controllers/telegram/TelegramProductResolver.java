package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.converters.TelegramProductConverter;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductList;
import ru.akvine.fitstats.services.ProductService;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.ProductBean;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramProductResolver {
    private final ProductService productService;
    private final TelegramProductConverter telegramProductConverter;

    public SendMessage list(TelegramProductList telegramProductList) {
        List<ProductBean> products = productService.findByFilter(new Filter().setFilterName(telegramProductList.getFilter()));
        return telegramProductConverter.convertToProductListResponse(telegramProductList.getChatId(), products);
    }
}
