package ru.akvine.fitstats.services.processors.statistic.additional;

import org.springframework.stereotype.Component;
import ru.akvine.fitstats.services.dto.product.ProductBean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ModeStatisticProcessor {
    public Map<String, Integer> calculate(List<ProductBean> products, int limit) {
        Map<String, Integer> mode = products
                .stream()
                .collect(Collectors.groupingBy(ProductBean::getTitle, Collectors.summingInt(e -> 1)));
        return mode
                .entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
