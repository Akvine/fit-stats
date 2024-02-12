package ru.akvine.fitstats.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.exceptions.product.ProductUuidGenerationLimitException;
import ru.akvine.fitstats.repositories.ProductRepository;
import ru.akvine.fitstats.services.properties.PropertyParseService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductUuidGeneratorService {
    private final ProductRepository productRepository;
    private final PropertyParseService propertyParseService;

    @Value("product.uuid.length")
    private String length;
    @Value("product.uuid.max.length.generation.attempts")
    private String maxAttempts;

    public String generate() {
        int maxAttempts = propertyParseService.parseInteger(this.maxAttempts);

        for (int i = 0; i < maxAttempts; ++i) {
            String code = RandomStringUtils.randomNumeric(propertyParseService.parseInteger(length));
            boolean exists = productRepository.findByUuid(code).isPresent();

            if (!exists) {
                return code;
            }
        }

        throw new ProductUuidGenerationLimitException("Attempts to generate product UUID have been exhausted!");
    }
}
