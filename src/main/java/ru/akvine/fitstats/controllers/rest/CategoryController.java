package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.CategoryConverter;
import ru.akvine.fitstats.controllers.rest.dto.category.CategoryListRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.meta.CategoryControllerMeta;
import ru.akvine.fitstats.services.CategoryService;
import ru.akvine.fitstats.services.dto.category.CategoryBean;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerMeta {
    private final CategoryConverter categoryConverter;
    private final CategoryService categoryService;

    @Override
    public Response list(@Valid CategoryListRequest request) {
        CategoryBean categoryBean = categoryConverter.convertToCategoryBean(request);
        List<CategoryBean> categories = categoryService.findByFilter(categoryBean);
        return categoryConverter.convertToCategoryListResponse(categories);
    }
}
