package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.repositories.CategoryRepository;
import ru.akvine.fitstats.services.dto.category.CategoryBean;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryBean> findByFilter(CategoryBean categoryBean) {
        Preconditions.checkNotNull(categoryBean, "categoryBean is null");
        logger.info("List categories by filter = {}", categoryBean.getTitle());

        String filter = categoryBean.getTitle();
        if (StringUtils.isNotBlank(filter)) {
            return categoryRepository
                    .findAll()
                    .stream()
                    .filter(category -> category.getTitle().toLowerCase().contains(filter))
                    .map(CategoryBean::new)
                    .collect(Collectors.toList());
        } else {
            return list();
        }
    }

    public List<CategoryBean> list() {
        return categoryRepository
                .findAll()
                .stream()
                .map(CategoryBean::new)
                .collect(Collectors.toList());
    }
}
