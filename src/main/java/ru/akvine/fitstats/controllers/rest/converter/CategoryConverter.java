package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.category.CategoryDto;
import ru.akvine.fitstats.controllers.rest.dto.category.CategoryListRequest;
import ru.akvine.fitstats.controllers.rest.dto.category.CategoryListResponse;
import ru.akvine.fitstats.services.dto.category.CategoryBean;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryConverter {
    public CategoryBean convertToCategoryBean(CategoryListRequest request) {
        Preconditions.checkNotNull(request, "categoryListRequest is null");
        return new CategoryBean()
                .setTitle(request.getFilter());
    }

    public CategoryListResponse convertToCategoryListResponse(List<CategoryBean> categories) {
        Preconditions.checkNotNull(categories, "categories is null");
        return new CategoryListResponse()
                .setCategories(categories.stream().map(this::buildCategoryDto).collect(Collectors.toList()));
    }

    private CategoryDto buildCategoryDto(CategoryBean categoryBean) {
        return new CategoryDto()
                .setTitle(categoryBean.getTitle());
    }
}
