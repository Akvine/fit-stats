package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.services.DietService;
import ru.akvine.fitstats.services.ProductService;
import ru.akvine.fitstats.services.dto.diet.AddDietRecordFinish;
import ru.akvine.fitstats.services.dto.diet.AddDietRecordStart;
import ru.akvine.fitstats.services.dto.diet.DietDisplay;
import ru.akvine.fitstats.services.dto.diet.Display;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.ProductBean;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TelegramServicesAdapter {
    private final ProductService productService;
    private final DietService dietService;

    public List<ProductBean> list(String filter) {
        return productService.findByFilter(new Filter().setFilterName(filter));
    }

    public DietDisplay display(Display display) {
        return dietService.display(display);
    }

    public AddDietRecordFinish addDietRecord(AddDietRecordStart addDietRecordStart) {
        return dietService.add(addDietRecordStart);
    }
}
