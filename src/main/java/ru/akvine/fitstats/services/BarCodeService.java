package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.entities.BarCodeEntity;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.exceptions.barcode.BarCodeAlreadyExistsException;
import ru.akvine.fitstats.exceptions.barcode.BarCodeNotFoundException;
import ru.akvine.fitstats.exceptions.product.ProductNotFoundException;
import ru.akvine.fitstats.repositories.BarCodeRepository;
import ru.akvine.fitstats.repositories.ProductRepository;
import ru.akvine.fitstats.services.dto.barcode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BarCodeService {
    private final BarCodeRepository barCodeRepository;
    private final ProductRepository productRepository;

    @Transactional
    public BarCodeBean get(GetBarCode getBarCode) {
        Preconditions.checkNotNull(getBarCode, "getBarCode is null");

        String clientUuid = getBarCode.getClientUuid();
        String number = getBarCode.getNumber();
        logger.info("Get barcode with number = {} by client with uuid = {}", number, clientUuid);

        return barCodeRepository
                .findByNumber(number)
                .map(BarCodeBean::new)
                .orElseThrow(() -> new BarCodeNotFoundException("Bar code with number = [" + number + "] not found!"));
    }


    public BarCodeBean add(AddBarCode addBarCode) {
        Preconditions.checkNotNull(addBarCode, "addBarCode is null");

        String clientUuid = addBarCode.getClientUuid();
        String productUuid = addBarCode.getProductUuid();
        String number = addBarCode.getNumber();
        logger.info("Add barcode with number = {} by client with uuid = {}", number, clientUuid);

        Optional<BarCodeEntity> barCodeEntityOptional = barCodeRepository.findByNumber(addBarCode.getNumber());
        if (barCodeEntityOptional.isPresent()) {
            throw new BarCodeAlreadyExistsException("Bar code with number = [" + addBarCode.getNumber() + "] already exists!");
        }

        ProductEntity productEntity = verifyProductExistsAndGet(productUuid);
        BarCodeEntity barCodeEntity = new BarCodeEntity()
                .setNumber(number)
                .setType(addBarCode.getBarCodeType())
                .setProduct(productEntity);

        BarCodeEntity createdBarCode = barCodeRepository.save(barCodeEntity);
        logger.info("Successful create bar code with number = {} by client with uuid = {}", number, clientUuid);
        return new BarCodeBean(createdBarCode);
    }

    public List<BarCodeBean> list(ListBarCode listBarCode) {
        Preconditions.checkNotNull(listBarCode, "listBarCode is null");

        String clientUuid = listBarCode.getClientUuid();
        String productUuid = listBarCode.getProductUuid();
        logger.info("List bar codes for product with uuid = {} by client with uuid = {}", productUuid, clientUuid);

        return barCodeRepository
                .findByProductUuid(productUuid)
                .stream()
                .map(BarCodeBean::new)
                .collect(Collectors.toList());
    }

    public BarCodeBean update(UpdateBarCode updateBarCode) {
        Preconditions.checkNotNull(updateBarCode, "updateBarCode is null");

        logger.info("Update barcode by = [{}]", updateBarCode);
        verifyProductExistsAndGet(updateBarCode.getProductUuid());
        BarCodeEntity barCodeEntity = verifyExistsAndGet(updateBarCode.getNumber());

        if (StringUtils.isNotBlank(updateBarCode.getNewNumber())) {
            barCodeEntity.setNumber(updateBarCode.getNewNumber());
        }

        if (updateBarCode.getType() != null) {
            barCodeEntity.setType(updateBarCode.getType());
        }

        barCodeEntity.setUpdatedDate(LocalDateTime.now());
        logger.info("Successful update barcode by = [{}]", updateBarCode);
        return new BarCodeBean(barCodeEntity);
    }

    public void delete(DeleteBarCode deleteBarCode) {
        Preconditions.checkNotNull(deleteBarCode, "deleteBarCode is null");

        logger.info("Delete barcode by [{}]", deleteBarCode);
        verifyProductExistsAndGet(deleteBarCode.getProductUuid());
        BarCodeEntity barCodeEntity = verifyExistsAndGet(deleteBarCode.getNumber());

        barCodeEntity.setDeletedDate(LocalDateTime.now());
        barCodeEntity.setDeleted(true);

        barCodeRepository.save(barCodeEntity);
        logger.info("Successful delete barcode by [{}]", deleteBarCode);
    }

    private BarCodeEntity verifyExistsAndGet(String number) {
        Preconditions.checkNotNull(number, "number is null");
        return barCodeRepository
                .findByNumber(number)
                .orElseThrow(() -> new BarCodeNotFoundException("Bar code with number = [" + number + "] not found!"));
    }


    private ProductEntity verifyProductExistsAndGet(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return productRepository
                .findByUuidAndNotDeleted(uuid)
                .orElseThrow(() -> new ProductNotFoundException("Product with uuid = [" + uuid + "] not found!"));
    }
}
