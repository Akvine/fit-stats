package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.controllers.rest.dto.admin.ImportProducts;
import ru.akvine.fitstats.controllers.rest.dto.admin.file.ProductCsvRow;
import ru.akvine.fitstats.entities.security.BlockedCredentialsEntity;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.admin.*;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;
import ru.akvine.fitstats.services.processors.format.Converter;
import ru.akvine.fitstats.services.security.BlockingService;
import ru.akvine.fitstats.utils.DateUtils;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class AdminService {
    private final ProductService productService;
    private final BlockingService blockingService;
    private final ClientService clientService;
    private final Map<ConverterType, Converter> availableConverters;

    @Autowired
    public AdminService(ProductService productService,
                        BlockingService blockingService,
                        ClientService clientService,
                        List<Converter> converters) {
        this.productService = productService;
        this.blockingService = blockingService;
        this.clientService = clientService;
        this.availableConverters = converters
                .stream()
                .collect(toMap(Converter::getType, identity()));
    }

    public byte[] exportProducts(ConverterType converterType) {
        Preconditions.checkNotNull(converterType, "converterType is null");
        List<ProductExport> productsExport = productService
                .getAll()
                .stream()
                .map(ProductExport::new)
                .collect(Collectors.toList());
        return availableConverters
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
                        .setVolume(Double.parseDouble(csvRow.getVolume()))
                        .setTitle(csvRow.getTitle())
                        .setProducer(csvRow.getProducer())
                        .setMeasurement(VolumeMeasurement.safeValueOf(csvRow.getMeasurement()));
                productService.add(productBean);
            }
        });
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
