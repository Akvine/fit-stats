package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.controllers.rest.dto.admin.product.ImportProducts;
import ru.akvine.fitstats.controllers.rest.dto.admin.product.file.ProductCsvRow;
import ru.akvine.fitstats.controllers.rest.dto.admin.product.file.ProductXlsxRow;
import ru.akvine.fitstats.entities.security.BlockedCredentialsEntity;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.managers.ConvertersManager;
import ru.akvine.fitstats.services.client.ClientService;
import ru.akvine.fitstats.services.dto.admin.*;
import ru.akvine.fitstats.services.dto.barcode.BarCodeBean;
import ru.akvine.fitstats.services.dto.barcode.DeleteBarCode;
import ru.akvine.fitstats.services.dto.barcode.UpdateBarCode;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;
import ru.akvine.fitstats.services.security.BlockingService;
import ru.akvine.fitstats.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.akvine.fitstats.constants.MacronutrientsConstants.DEFAULT_VOLUME;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final ProductService productService;
    private final BarCodeService barCodeService;
    private final BlockingService blockingService;
    private final ClientService clientService;
    private final ConvertersManager convertersManager;

    public byte[] exportProducts(ConverterType converterType) {
        Preconditions.checkNotNull(converterType, "converterType is null");
        List<ProductExport> productsExport = productService
                .getAll()
                .stream()
                .map(ProductExport::new)
                .collect(Collectors.toList());
        return convertersManager
                .getConverters()
                .get(converterType)
                .convert(productsExport, ProductExport.class);
    }

    public void importProducts(ImportProducts importProducts) {
        Preconditions.checkNotNull(importProducts, "importProducts is null");
        logger.info("Import products by clientUuid = {}", importProducts.getClientUuid());

        List<?> records = importProducts.getRecords();
        records.forEach(record -> {
            if (record instanceof ProductCsvRow) {
                ProductCsvRow csvRow = (ProductCsvRow) record;
                ProductBean productBean = new ProductBean()
                        .setUuid(csvRow.getUuid())
                        .setProteins(Double.parseDouble(csvRow.getProteins()))
                        .setFats(Double.parseDouble(csvRow.getFats()))
                        .setCarbohydrates(Double.parseDouble(csvRow.getCarbohydrates()))
                        .setAlcohol(Double.parseDouble(csvRow.getAlcohol()))
                        .setVol(Double.parseDouble(csvRow.getVol()))
                        .setVolume(DEFAULT_VOLUME)
                        .setTitle(csvRow.getTitle())
                        .setProducer(csvRow.getProducer())
                        .setMeasurement(VolumeMeasurement.safeValueOf(csvRow.getMeasurement()));
                productService.add(productBean);
            } else {
                ProductXlsxRow xlsxRow = (ProductXlsxRow) record;
                ProductBean productBean = new ProductBean()
                        .setUuid(xlsxRow.getUuid())
                        .setProteins(Double.parseDouble(xlsxRow.getProteins()))
                        .setFats(Double.parseDouble(xlsxRow.getFats()))
                        .setCarbohydrates(Double.parseDouble(xlsxRow.getCarbohydrates()))
                        .setAlcohol(Double.parseDouble(xlsxRow.getAlcohol()))
                        .setVol(Double.parseDouble(xlsxRow.getVol()))
                        .setVolume(DEFAULT_VOLUME)
                        .setTitle(xlsxRow.getTitle())
                        .setProducer(xlsxRow.getProducer())
                        .setMeasurement(VolumeMeasurement.safeValueOf(xlsxRow.getMeasurement()));
                productService.add(productBean);
            }
        });
    }

    public BarCodeBean updateBarCode(UpdateBarCode updateBarCode) {
        return barCodeService.update(updateBarCode);
    }

    public void deleteBarCode(DeleteBarCode deleteBarCode) {
        barCodeService.delete(deleteBarCode);
    }

    public ProductBean updateProduct(UpdateProduct updateProduct) {
        Preconditions.checkNotNull(updateProduct, "updateProduct is null");
        logger.info("Update product by clientUuid = {}", updateProduct.getClientUuid());
        return productService.update(updateProduct);
    }

    public void deleteProduct(String productUuid) {
        Preconditions.checkNotNull(productUuid, "product uuid is null");
        productService.deleteByUuid(productUuid);
    }

    public BlockClientFinish blockClient(BlockClientStart start) {
        Preconditions.checkNotNull(start, "blockClientStart is null");
        long minutes = start.getMinutes();

        String email = start.getEmail();
        if (StringUtils.isBlank(email)) {
            email = clientService
                    .getByUuid(start.getUuid())
                    .getEmail();
        }

        LocalDateTime blockDate = LocalDateTime.now().plusMinutes(minutes);
        logger.info("Block client with email = {} until date = {}", email, blockDate);
        blockingService.setBlock(email, minutes);

        logger.info("Successful block client with email = {} until date = {}", email, blockDate);
        return new BlockClientFinish()
                .setEmail(email)
                .setDateTime(blockDate)
                .setMinutes(minutes);
    }

    public List<BlockClientEntry> listBlocked(String email) {
        logger.info("List blocked clients by client with email = {}", email);

        List<BlockedCredentialsEntity> list = blockingService.list();
        return list.stream().map(obj -> {
            LocalDateTime start = obj.getBlockStartDate();
            LocalDateTime end = obj.getBlockEndDate();
            String blockedEmail = obj.getLogin();
            long minutes = DateUtils.getMinutes(start, end);
            return new BlockClientEntry()
                    .setEmail(blockedEmail)
                    .setBlockStartDate(start)
                    .setBlockEndDate(end)
                    .setMinutes(minutes);
        }).collect(Collectors.toList());
    }

    public void unblockClient(UnblockClient unblockClient) {
        Preconditions.checkNotNull(unblockClient, "unblockClient is null");

        String email = unblockClient.getEmail();
        if (StringUtils.isBlank(email)) {
            email = clientService
                    .getByUuid(unblockClient.getUuid())
                    .getEmail();
        }

        logger.info("Unblock client with email = {} ", email);
        blockingService.removeBlock(email);
        logger.info("Successful unblock client with email = {}", email);
    }
}
