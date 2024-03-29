package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.services.dto.product.GetByBarCode;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.exceptions.product.ProductNotFoundException;
import ru.akvine.fitstats.repositories.ProductRepository;
import ru.akvine.fitstats.repositories.specifications.ProductSpecification;
import ru.akvine.fitstats.services.dto.barcode.BarCodeBean;
import ru.akvine.fitstats.services.dto.barcode.GetBarCode;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BarCodeService barCodeService;
    private final MacronutrientsCalculationService macronutrientsCalculationService;
    private final ProductUuidGeneratorService productUuidGeneratorService;

    public ProductBean add(ProductBean productBean) {
        Preconditions.checkNotNull(productBean, "productBean is null");
        logger.info("Try to add product by product bean = [{}]", productBean);

        ProductEntity productEntity = new ProductEntity()
                .setUuid(StringUtils.isBlank(productBean.getUuid()) ?
                        productUuidGeneratorService.generate() : productBean.getUuid())
                .setTitle(productBean.getTitle())
                .setProducer(productBean.getProducer())
                .setProteins(productBean.getProteins())
                .setFats(productBean.getFats())
                .setCarbohydrates(productBean.getCarbohydrates())
                .setAlcohol(productBean.getAlcohol())
                .setVol(productBean.getVol())
                .setCalories(productBean.getCalories())
                .setVolume(productBean.getVolume())
                .setMeasurement(productBean.getMeasurement())
                .setCalories(macronutrientsCalculationService.calculateCalories(
                        productBean.getProteins(),
                        productBean.getFats(),
                        productBean.getCarbohydrates(),
                        productBean.getAlcohol()
                ));

        ProductBean savedProductBean = new ProductBean(productRepository.save(productEntity));
        logger.info("Successful save product bean = [{}]", savedProductBean);
        return savedProductBean;
    }

    public ProductBean getByBarCodeNumber(GetByBarCode getByBarCode) {
        Preconditions.checkNotNull(getByBarCode, "getByBarCode is null");

        String clientUuid = getByBarCode.getClientUuid();
        String barCodeNumber = getByBarCode.getNumber();
        logger.info("Get product with barcode number = {} by client with uuid = {}", barCodeNumber, clientUuid);

        GetBarCode getBarCode = new GetBarCode()
                .setClientUuid(clientUuid)
                .setNumber(barCodeNumber);
        BarCodeBean barCodeBean = barCodeService.get(getBarCode);

        return barCodeBean.getProductBean();
    }

    public ProductBean update(UpdateProduct updateProduct) {
        Preconditions.checkNotNull(updateProduct, "updateProduct is null");
        logger.info("Try to update product = [{}] by client with uuid = {}", updateProduct, updateProduct.getClientUuid());
        ProductEntity productEntity = verifyExistsAndGet(updateProduct.getUuid());

        boolean macronutrientsUpdated = false;
        if (StringUtils.isNotBlank(updateProduct.getTitle())) {
            productEntity.setTitle(updateProduct.getTitle());
        }
        if (StringUtils.isNotBlank(updateProduct.getProducer())) {
            productEntity.setProducer(updateProduct.getProducer());
        }
        if (updateProduct.getMeasurement() != null) {
            productEntity.setMeasurement(updateProduct.getMeasurement());
        }
        if (updateProduct.getProteins() != null) {
            macronutrientsUpdated = true;
            productEntity.setProteins(updateProduct.getProteins());
        }
        if (updateProduct.getFats() != null) {
            macronutrientsUpdated = true;
            productEntity.setFats(updateProduct.getFats());
        }
        if (updateProduct.getCarbohydrates() != null) {
            macronutrientsUpdated = true;
            productEntity.setCarbohydrates(updateProduct.getCarbohydrates());
        }
        if (updateProduct.getVolume() != null) {
            productEntity.setVolume(updateProduct.getVolume());
        }
        if (updateProduct.getVol() != null) {
            productEntity.setVol(updateProduct.getVol());
        }

        if (macronutrientsUpdated) {
            productEntity.setCalories(
                    macronutrientsCalculationService.calculateCalories(
                            productEntity.getProteins(),
                            productEntity.getFats(),
                            productEntity.getCarbohydrates(),
                            productEntity.getAlcohol()
                    )
            );
        }

        productEntity.setUpdatedDate(LocalDateTime.now());
        ProductBean savedProductBean = new ProductBean(productRepository.save(productEntity));
        logger.info("Successful save product = [{}]", savedProductBean);
        return savedProductBean;
    }

    public void deleteByUuid(String uuid) {
        Preconditions.checkNotNull(uuid, "product uuid is null");
        logger.info("Delete product by uuid = {}", uuid);
        ProductEntity productEntity = verifyExistsAndGet(uuid);

        productEntity.setDeleted(true);
        productEntity.setDeletedDate(LocalDateTime.now());

        productRepository.save(productEntity);
    }

    public List<ProductBean> getAll() {
        logger.info("Get all products");
        return findByFilter(new Filter());
    }

    @Transactional
    public List<ProductBean> findByFilter(Filter filter) {
        Specification<ProductEntity> specification = ProductSpecification.build(filter);
        return productRepository
                .findAll(specification)
                .stream()
                .map(ProductBean::new)
                .collect(Collectors.toList());
    }

    public ProductEntity verifyExistsAndGet(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return productRepository
                .findByUuidAndNotDeleted(uuid)
                .orElseThrow(() -> new ProductNotFoundException("Product with uuid = [" + uuid + "] not found!"));
    }

    public List<ProductEntity> verifyExistsByPartialUuidAndGet(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return productRepository.findByPartialUuidAndNotDeleted(uuid);

    }

    public ProductEntity findByUuid(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return productRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ProductNotFoundException("Product with uuid = [" + uuid + "] not found!"));
    }
}
