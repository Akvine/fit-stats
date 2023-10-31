package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.exceptions.product.ProductNotFoundException;
import ru.akvine.fitstats.repositories.ProductRepository;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.utils.DietUtils;
import ru.akvine.fitstats.utils.UUIDGenerator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private static final String EMPTY_SPACE = " ";

    @Value("${uuid.length}")
    private int uuidLength;

    public ProductBean add(ProductBean productBean) {
        Preconditions.checkNotNull(productBean, "productBean is null");

        ProductEntity productEntity = new ProductEntity()
                .setUuid(UUIDGenerator.uuidWithoutDashes(uuidLength))
                .setTitle(productBean.getTitle())
                .setProducer(productBean.getProducer())
                .setProteins(productBean.getProteins())
                .setFats(productBean.getFats())
                .setCarbohydrates(productBean.getCarbohydrates())
                .setCalories(productBean.getCalories())
                .setVolume(productBean.getVolume())
                .setMeasurement(productBean.getMeasurement())
                .setCalories(DietUtils.calculateCalories(
                        productBean.getProteins(),
                        productBean.getFats(),
                        productBean.getCarbohydrates()));
        return new ProductBean(productRepository.save(productEntity));
    }

    public List<ProductBean> findByFilter(String filter) {
        if (StringUtils.isBlank(filter)) {
            return productRepository
                    .findAll()
                    .stream()
                    .map(ProductBean::new)
                    .collect(Collectors.toList());
        } else {
            return productRepository
                    .findAll()
                    .stream()
                    .filter(product -> {
                        String title = product.getTitle();
                        String producer = product.getProducer();
                        boolean titleContains = false;
                        boolean producerContains = false;

                        String[] titleWords = product
                                .getTitle()
                                .toLowerCase()
                                .split(EMPTY_SPACE);
                        String[] productWords = product
                                .getProducer()
                                .toLowerCase()
                                .split(EMPTY_SPACE);
                        for (String word : titleWords) {
                            if (word.contains(filter.toLowerCase())) {
                                titleContains = true;
                                break;
                            }
                        }
                        for (String word : productWords) {
                            if (word.contains(filter.toLowerCase())) {
                                producerContains = true;
                                break;
                            }
                        }
                        return titleContains || producerContains;
                    })
                    .map(ProductBean::new)
                    .collect(Collectors.toList());
        }
    }

    public ProductBean getByUuid(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return new ProductBean(verifyExistsAndGet(uuid));
    }

    public ProductEntity verifyExistsAndGet(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return productRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ProductNotFoundException("Product with uuid = [" + uuid + "] not found!"));
    }
}
