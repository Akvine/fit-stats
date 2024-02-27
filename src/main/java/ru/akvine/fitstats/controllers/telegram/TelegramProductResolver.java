package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.rest.converter.scanner.barcode.BarCodeScanResult;
import ru.akvine.fitstats.controllers.rest.converter.scanner.barcode.BarCodeScanner;
import ru.akvine.fitstats.controllers.telegram.converters.TelegramProductConverter;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductAddRequest;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductGetByBarCodeRequest;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductListRequest;
import ru.akvine.fitstats.controllers.telegram.validators.TelegramProductValidator;
import ru.akvine.fitstats.services.ProductService;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.GetByBarCode;
import ru.akvine.fitstats.services.dto.product.ProductBean;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramProductResolver {
    private final ProductService productService;
    private final BarCodeScanner barCodeScanner;
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

    public SendMessage getByBarCode(TelegramProductGetByBarCodeRequest request) {
        BarCodeScanResult result = barCodeScanner.scan(request.getPhoto());
        GetByBarCode getByBarCode = telegramProductConverter.convertToGetByBarCode(
                request.getClientUuid(),
                result);
        ProductBean productBean = productService.getByBarCodeNumber(getByBarCode);
        return telegramProductConverter.convertToProductListResponse(request.getChatId(), List.of(productBean));
    }
}
