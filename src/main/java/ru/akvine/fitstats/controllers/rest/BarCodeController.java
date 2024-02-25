package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.converter.BarCodeConverter;
import ru.akvine.fitstats.controllers.rest.converter.scanner.barcode.BarCodeScanResult;
import ru.akvine.fitstats.controllers.rest.converter.scanner.barcode.BarCodeScanner;
import ru.akvine.fitstats.controllers.rest.dto.barcode.AddBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.barcode.GetBarCodeByNumberRequest;
import ru.akvine.fitstats.controllers.rest.dto.barcode.ListBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.meta.BarCodeControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.BarCodeValidator;
import ru.akvine.fitstats.services.BarCodeService;
import ru.akvine.fitstats.services.dto.barcode.AddBarCode;
import ru.akvine.fitstats.services.dto.barcode.BarCodeBean;
import ru.akvine.fitstats.services.dto.barcode.GetBarCode;
import ru.akvine.fitstats.services.dto.barcode.ListBarCode;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BarCodeController implements BarCodeControllerMeta {
    private final BarCodeValidator barCodeValidator;
    private final BarCodeService barCodeService;
    private final BarCodeConverter barCodeConverter;
    private final BarCodeScanner barCodeScanner;

    @Override
    public Response add(@Valid AddBarCodeRequest request) {
        barCodeValidator.verifyAddBarCodeRequest(request);
        AddBarCode addBarCode = barCodeConverter.convertToAddBarCode(request);
        BarCodeBean barCodeBean = barCodeService.add(addBarCode);
        return barCodeConverter.convertToAddBarCodeResponse(barCodeBean);
    }

    @Override
    public Response add(String productUuid, MultipartFile photo) {
        BarCodeScanResult result = barCodeScanner.scan(photo);
        AddBarCode addBarCode = barCodeConverter.convertToAddBarCode(productUuid, result);
        BarCodeBean barCodeBean = barCodeService.add(addBarCode);
        return barCodeConverter.convertToGetBarCodeResponse(barCodeBean);
    }

    @Override
    public Response list(@Valid ListBarCodeRequest request) {
        ListBarCode listBarCode = barCodeConverter.convertToListBarCode(request);
        List<BarCodeBean> barCodeBeans = barCodeService.list(listBarCode);
        return barCodeConverter.convertToListBarCodeResponse(barCodeBeans);
    }

    @Override
    public Response get(@Valid GetBarCodeByNumberRequest request) {
        GetBarCode getBarCode = barCodeConverter.convertToGetBarCode(request);
        BarCodeBean barCodeBean = barCodeService.get(getBarCode);
        return barCodeConverter.convertToGetBarCodeResponse(barCodeBean);
    }

    @Override
    public Response get(MultipartFile photo) {
        BarCodeScanResult result = barCodeScanner.scan(photo);
        GetBarCode getBarCode = barCodeConverter.convertToGetBarCode(result);
        BarCodeBean barCodeBean = barCodeService.get(getBarCode);
        return barCodeConverter.convertToGetBarCodeResponse(barCodeBean);
    }
}
