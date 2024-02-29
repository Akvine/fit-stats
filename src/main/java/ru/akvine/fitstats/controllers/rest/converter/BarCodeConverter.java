package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.converter.scanner.barcode.BarCodeScanResult;
import ru.akvine.fitstats.controllers.rest.dto.barcode.*;
import ru.akvine.fitstats.enums.BarCodeType;
import ru.akvine.fitstats.services.dto.barcode.AddBarCode;
import ru.akvine.fitstats.services.dto.barcode.BarCodeBean;
import ru.akvine.fitstats.services.dto.barcode.GetBarCode;
import ru.akvine.fitstats.services.dto.barcode.ListBarCode;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BarCodeConverter {
    public AddBarCode convertToAddBarCode(AddBarCodeRequest request) {
        Preconditions.checkNotNull(request, "addBarCodeRequest is null");
        return new AddBarCode()
                .setBarCodeType(StringUtils.isBlank(request.getType()) ?  null : BarCodeType.valueOf(request.getType()))
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setNumber(request.getNumber())
                .setProductUuid(request.getProductUuid());
    }

    public AddBarCode convertToAddBarCode(String productUuid, BarCodeScanResult barCodeScanResult) {
        Preconditions.checkNotNull(productUuid, "productUuid is null");
        Preconditions.checkNotNull(barCodeScanResult, "barCodeScanResult is null");
        return new AddBarCode()
                .setNumber(barCodeScanResult.getNumber())
                .setBarCodeType(barCodeScanResult.getBarCodeType())
                .setProductUuid(productUuid)
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid());
    }

    public ListBarCode convertToListBarCode(ListBarCodeRequest request) {
        Preconditions.checkNotNull(request, "listBarCodeRequest is null");
        return new ListBarCode()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setProductUuid(request.getProductUuid());
    }

    public GetBarCode convertToGetBarCode(GetBarCodeByNumberRequest request) {
        Preconditions.checkNotNull(request, "getBarCodeByNumberRequest is null");
        return new GetBarCode()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setNumber(request.getNumber());
    }

    public GetBarCode convertToGetBarCode(BarCodeScanResult result) {
        Preconditions.checkNotNull(result, "barCodeScanResult is null");
        return new GetBarCode()
                .setNumber(result.getNumber())
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid());
    }

    public AddBarCodeResponse convertToAddBarCodeResponse(BarCodeBean barCodeBean) {
        Preconditions.checkNotNull(barCodeBean, "barCodeBean is null");
        return new AddBarCodeResponse()
                .setBarCode(buildBarCodeDto(barCodeBean));
    }

    public GetBarCodeResponse convertToGetBarCodeResponse(BarCodeBean barCodeBean) {
        Preconditions.checkNotNull(barCodeBean, "barCodeBean is null");
        return new GetBarCodeResponse()
                .setBarCodeDto(buildBarCodeDto(barCodeBean));
    }

    public ListBarCodeResponse convertToListBarCodeResponse(List<BarCodeBean> barCodeBeans) {
        Preconditions.checkNotNull(barCodeBeans, "barCodeBeans is null");
        return new ListBarCodeResponse()
                .setBarCodes(barCodeBeans
                        .stream()
                        .map(this::buildBarCodeDto)
                        .collect(Collectors.toList()));
    }

    private BarCodeDto buildBarCodeDto(BarCodeBean barCodeBean) {
        return new BarCodeDto()
                .setNumber(barCodeBean.getNumber())
                .setType(barCodeBean.getBarCodeType() != null ? barCodeBean.getBarCodeType().name() : null);
    }
}
