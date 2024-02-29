package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.controllers.rest.dto.admin.barcode.DeleteBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.barcode.UpdateBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.barcode.UpdateBarCodeResponse;
import ru.akvine.fitstats.controllers.rest.dto.admin.client.*;
import ru.akvine.fitstats.controllers.rest.dto.admin.product.*;
import ru.akvine.fitstats.controllers.rest.dto.admin.product.file.ProductCsvRow;
import ru.akvine.fitstats.controllers.rest.dto.admin.product.file.ProductXlsxRow;
import ru.akvine.fitstats.controllers.rest.dto.product.ProductDto;
import ru.akvine.fitstats.controllers.rest.dto.product.ProductResponse;
import ru.akvine.fitstats.enums.BarCodeType;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.managers.ParsersManager;
import ru.akvine.fitstats.services.dto.admin.BlockClientEntry;
import ru.akvine.fitstats.services.dto.admin.BlockClientFinish;
import ru.akvine.fitstats.services.dto.admin.BlockClientStart;
import ru.akvine.fitstats.services.dto.admin.UnblockClient;
import ru.akvine.fitstats.services.dto.barcode.BarCodeBean;
import ru.akvine.fitstats.services.dto.barcode.DeleteBarCode;
import ru.akvine.fitstats.services.dto.barcode.UpdateBarCode;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;
import ru.akvine.fitstats.utils.DateUtils;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Component
@RequiredArgsConstructor
public class AdminConverter {
    private static final String HEADER_PREFIX = "attachment; filename=";
    private static final String DEFAULT_FILE_NAME = "file";
    private static final String POINT = ".";

    private static final int BLOCK_TIME_YEARS = 100;

    private final ParsersManager parsersManager;

    public ConverterType convertToConverterType(ExportProductsRequest request) {
        String converterType = request.getConverterType();
        if (StringUtils.isBlank(converterType)) {
            return ConverterType.CSV;
        }
        return ConverterType.valueOf(converterType.toUpperCase());
    }

    public ResponseEntity convertToExportResponse(String filename, byte[] file, ConverterType converterType) {
        Preconditions.checkNotNull(file, "products is null");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, resolveHeaderType(filename, converterType))
                .contentType(MediaType.parseMediaType(resolveMediaType(converterType)))
                .body(file);
    }

    public ImportProducts convertToImportProduct(String converterType, MultipartFile file) {
        Preconditions.checkNotNull(converterType, "convertType is null");
        Preconditions.checkNotNull(file, "file is null");

        ConverterType type = ConverterType.valueOf(converterType);
        return new ImportProducts()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setRecords(parsersManager
                        .getParsers()
                        .get(type)
                        .parse(file, resolveClass(type)));
    }

    public InvalidProductsRowsResponse convertToInvalidProductsRowsResponse(List<InvalidProductRow> invalidProductRows) {
        Preconditions.checkNotNull(invalidProductRows, "invalidProductRows is null");
        return new InvalidProductsRowsResponse()
                .setInvalidRows(invalidProductRows);
    }

    public UpdateProduct convertToUpdateProduct(UpdateProductRequest request) {
        return new UpdateProduct()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setUuid(request.getUuid())
                .setMeasurement(StringUtils.isBlank(request.getMeasurement()) ? null : VolumeMeasurement.safeValueOf(request.getMeasurement()))
                .setTitle(request.getTitle())
                .setProducer(request.getProducer())
                .setProteins(request.getProteins())
                .setFats(request.getFats())
                .setCarbohydrates(request.getCarbohydrates())
                .setVol(request.getVol())
                .setVolume(request.getVolume());
    }

    public ProductResponse convertToProductResponse(ProductBean productBean) {
        Preconditions.checkNotNull(productBean, "productBean is null");
        return new ProductResponse().setProduct(buildProductDto(productBean));
    }

    public BlockClientStart convertToBlockClientStart(BlockClientRequest request) {
        Preconditions.checkNotNull(request, "blockClientRequest is null");
        BlockClientStart start = new BlockClientStart();

        String email = request.getEmail();
        if (StringUtils.isNotBlank(email)) {
            start.setEmail(email);
        } else {
            start.setUuid(request.getUuid());
        }

        if (request.getDate() == null) {
            start.setMinutes(DateUtils.getMinutes(BLOCK_TIME_YEARS));
        } else {
            start.setMinutes(LocalDateTime.now().until(request.getDate(), ChronoUnit.MINUTES));
        }

        return start;
    }

    public BlockClientResponse convertToBlockClientResponse(BlockClientFinish finish) {
        Preconditions.checkNotNull(finish, "blockClientFinish is null");
        return new BlockClientResponse()
                .setEmail(finish.getEmail())
                .setDateTime(finish.getDateTime())
                .setMinutes(finish.getMinutes());
    }

    public ListBlockClientResponse convertToListBlockClientResponse(List<BlockClientEntry> blocked) {
        Preconditions.checkNotNull(blocked, "blockedClientEntries is null");
        return new ListBlockClientResponse()
                .setCount(blocked.size())
                .setList(blocked
                        .stream()
                        .map(this::buildBlockClientDto)
                        .collect(Collectors.toList()));
    }

    public UnblockClient convertToUnblockClient(UnblockClientRequest request) {
        Preconditions.checkNotNull(request, "unblockClientRequest");
        UnblockClient unblockClient = new UnblockClient();

        String email = request.getEmail();
        if (StringUtils.isNotBlank(email)) {
            unblockClient.setEmail(email);
        } else {
            unblockClient.setUuid(request.getUuid());
        }
        return unblockClient;
    }

    public UpdateBarCode convertToUpdateBarCode(UpdateBarCodeRequest request) {
        Preconditions.checkNotNull(request, "updateBarCodeRequest is null");

        return new UpdateBarCode()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setProductUuid(request.getProductUuid())
                .setNumber(request.getNumber())
                .setNewNumber(StringUtils.isNotBlank(request.getNewNumber()) ? request.getNewNumber() : null)
                .setType(StringUtils.isNotBlank(request.getType()) ? BarCodeType.valueOf(request.getType()) : null);
    }

    public UpdateBarCodeResponse convertToUpdateBarCodeResponse(BarCodeBean barCodeBean) {
        Preconditions.checkNotNull(barCodeBean, "barCodeBean is null");

        return new UpdateBarCodeResponse()
                .setNumber(barCodeBean.getNumber())
                .setProductUuid(barCodeBean.getProductBean().getUuid())
                .setType(barCodeBean.getBarCodeType().name());
    }

    public DeleteBarCode convertToDeleteBarCode(DeleteBarCodeRequest request) {
        Preconditions.checkNotNull(request, "deleteBarCodeRequest is null");
        return new DeleteBarCode()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setProductUuid(request.getProductUuid())
                .setNumber(request.getNumber());
    }


    private ProductDto buildProductDto(ProductBean productBean) {
        int roundAccuracy = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getRoundAccuracy();
        return new ProductDto()
                .setTitle(productBean.getTitle())
                .setProducer(productBean.getProducer())
                .setUuid(productBean.getUuid())
                .setProteins(round(productBean.getProteins(), roundAccuracy))
                .setFats(round(productBean.getFats(), roundAccuracy))
                .setCalories(round(productBean.getCalories(), roundAccuracy))
                .setCarbohydrates(round(productBean.getCarbohydrates(), roundAccuracy))
                .setMeasurement(productBean.getMeasurement().toString())
                .setVol(productBean.getVol())
                .setVolume(productBean.getVolume());
    }

    private BlockClientDto buildBlockClientDto(BlockClientEntry entry) {
        return new BlockClientDto()
                .setEmail(entry.getEmail())
                .setMinutes(entry.getMinutes())
                .setBlockStartDate(entry.getBlockStartDate())
                .setBlockEndDate(entry.getBlockEndDate());
    }

    private String resolveHeaderType(String filename, ConverterType converterType) {
        StringBuilder builder = new StringBuilder();
        builder.append(HEADER_PREFIX);

        if (StringUtils.isBlank(filename)) {
            builder
                    .append(DEFAULT_FILE_NAME);
        } else {
            builder
                    .append(filename);
        }
        builder.append(POINT);
        builder.append(converterType.getValue());
        return builder.toString();
    }

    private String resolveMediaType(ConverterType converterType) {
        switch (converterType) {
            case CSV:
                return "application/csv";
            case XLSX:
                return "application/xlsx";
            default:
                throw new IllegalArgumentException("Converter with type = [" + converterType + "] is not supported!");
        }
    }

    private Class resolveClass(ConverterType converterType) {
        switch (converterType) {
            case CSV:
                return ProductCsvRow.class;
            case XLSX:
                return ProductXlsxRow.class;
            default:
                throw new IllegalArgumentException("Converter with type = [" + converterType + "] is not supported!");
        }
    }
}
