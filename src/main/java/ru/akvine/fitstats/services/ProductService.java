package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.CategoryEntity;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.exceptions.category.CategoryNotFoundException;
import ru.akvine.fitstats.exceptions.product.ProductNotFoundException;
import ru.akvine.fitstats.repositories.CategoryRepository;
import ru.akvine.fitstats.repositories.ProductRepository;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;
import ru.akvine.fitstats.utils.DietUtils;
import ru.akvine.fitstats.utils.UUIDGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final String EMPTY_SPACE = " ";

    @Value("${uuid.length}")
    private int uuidLength;

    public ProductBean add(ProductBean productBean) {
        Preconditions.checkNotNull(productBean, "productBean is null");

        Set<String> categoriesTitles = productBean.getCategoriesTitles();
        Set<CategoryEntity> categories = categoryRepository.findByTitles(categoriesTitles);
        if (CollectionUtils.isEmpty(categories)) {
            String errorMessage = String.format("No one of the presented categories = %s were found", categoriesTitles);
            throw new CategoryNotFoundException(errorMessage);
        }
        ProductEntity productEntity = new ProductEntity()
                .setUuid(UUIDGenerator.uuidWithoutDashes(uuidLength))
                .setTitle(productBean.getTitle())
                .setProducer(productBean.getProducer())
                .setProteins(productBean.getProteins())
                .setFats(productBean.getFats())
                .setCarbohydrates(productBean.getCarbohydrates())
                .setVol(productBean.getVol())
                .setCalories(productBean.getCalories())
                .setVolume(productBean.getVolume())
                .setMeasurement(productBean.getMeasurement())
                .setCalories(DietUtils.calculateCalories(
                        productBean.getProteins(),
                        productBean.getFats(),
                        productBean.getCarbohydrates(),
                        productBean.getVol()))
                .setCategories(categories);
        return new ProductBean(productRepository.save(productEntity));
    }

    public ProductBean update(UpdateProduct updateProduct) {
        Preconditions.checkNotNull(updateProduct, "updateProduct is null");
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

        if (macronutrientsUpdated) {
            productEntity.setCalories(
                    DietUtils.calculateCalories(
                            productEntity.getProteins(),
                            productEntity.getFats(),
                            productEntity.getCarbohydrates(),
                            productEntity.getVol())
            );
        }

        productEntity.setUpdatedDate(LocalDateTime.now());
        return new ProductBean(productRepository.save(productEntity));
    }

    public void deleteByUuid(String uuid) {
        Preconditions.checkNotNull(uuid, "product uuid is null");
        ProductEntity productEntity = verifyExistsAndGet(uuid);

        productEntity.setDeleted(true);
        productEntity.setDeletedDate(LocalDateTime.now());

        productRepository.save(productEntity);
    }

    public List<ProductBean> getAll() {
        return findByFilter(new Filter());
    }

    public List<ProductBean> findByFilter(Filter filter) {
        List<ProductBean> filteredBeans;
        if (StringUtils.isBlank(filter.getFilterName())) {
            filteredBeans = findWithoutFilter();
        } else {
            filteredBeans = findWithFilter(filter.getFilterName());
        }

        if (filter.getMacronutrientsWithValues() != null && !filter.getMacronutrientsWithValues().isEmpty()) {
            AtomicReference<List<ProductBean>> superFiltered = new AtomicReference<>();
            filter
                    .getMacronutrientsWithValues()
                    .forEach((macronutrient, value) -> {
                        if (macronutrient.equals("fats")) {
                            superFiltered.set(filteredBeans
                                    .stream()
                                    .filter(product -> product.getFats() < value)
                                    .collect(Collectors.toList()));
                        }
                        if (macronutrient.equals("proteins")) {
                            superFiltered.set(filteredBeans
                                    .stream()
                                    .filter(product -> product.getProteins() < value)
                                    .collect(Collectors.toList()));
                        }
                        if (macronutrient.equals("carbohydrates")) {
                            superFiltered.set(filteredBeans
                                    .stream()
                                    .filter(product -> product.getCarbohydrates() < value)
                                    .collect(Collectors.toList()));
                        }
                        if (macronutrient.equals("calories")) {
                            superFiltered.set(filteredBeans
                                    .stream()
                                    .filter(product -> product.getCalories() < value)
                                    .collect(Collectors.toList()));
                        }
                    });
            return superFiltered.get();
        }
        return filteredBeans;
    }

    public ProductBean getByUuid(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return new ProductBean(verifyExistsAndGet(uuid));
    }

    public ProductEntity verifyExistsAndGet(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return productRepository
                .findByUuidAndNotDeleted(uuid)
                .orElseThrow(() -> new ProductNotFoundException("Product with uuid = [" + uuid + "] not found!"));
    }

    public ProductEntity findByUuid(String uuid) {
        Preconditions.checkNotNull(uuid, "uuid is null");
        return productRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ProductNotFoundException("Product with uuid = [" + uuid + "] not found!"));
    }

    private List<ProductBean> findWithoutFilter() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductBean::new)
                .collect(Collectors.toList());
    }

    private List<ProductBean> findWithFilter(String filter) {
        return productRepository
                .findAll()
                .stream()
                .filter(product -> {
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
