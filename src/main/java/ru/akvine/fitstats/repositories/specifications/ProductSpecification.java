package ru.akvine.fitstats.repositories.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.MacronutrientFilterPart;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.services.dto.product.Filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ProductSpecification {

    public static Specification<ProductEntity> build(Filter filter) {

        Specification<ProductEntity> specification = notDeleted();
        specification = specification.and(withFilterName(filter.getFilterName()));

        if (filter.getMacronutrientFilterParts() != null) {
            specification = specification.and(withMacronutrientFilter(filter.getMacronutrientFilterParts()));
        }

        return specification;
    }

    private static Specification<ProductEntity> notDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.isFalse(root.get("deleted")),
                criteriaBuilder.isNull(root.get("deletedDate"))
        );
    }

    private static Specification<ProductEntity> withFilterName(String filter) {
        return (Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (filter == null || filter.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String[] keywords = filter.split("\\s+");
            List<Predicate> predicates = new ArrayList<>();

            Arrays.stream(keywords)
                    .map(keyword -> criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("uuid")), "%" + keyword.toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("producer")), "%" + keyword.toLowerCase() + "%")
                    ))
                    .forEach(predicates::add);

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    private static Specification<ProductEntity> withMacronutrientFilter(List<MacronutrientFilterPart> parts) {
        return (Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<String> operators = new ArrayList<>();

            for (MacronutrientFilterPart filter : parts) {
                String macronutrient = filter.getMacronutrient();
                String condition = filter.getCondition();
                Double value = Double.parseDouble(filter.getValue());
                String logicCondition = filter.getLogicCondition();

                if (logicCondition != null) {
                    operators.add(logicCondition);
                }

                Predicate macronutrientPredicate = buildMacronutrientPredicate(root, criteriaBuilder, macronutrient, condition, value);
                predicates.add(macronutrientPredicate);
            }

            return combinePredicatesWithOperators(predicates, operators, criteriaBuilder);
        };
    }

    private static Predicate buildMacronutrientPredicate(Root<ProductEntity> root, CriteriaBuilder criteriaBuilder,
                                                         String macronutrient, String condition, Double value) {
        switch (condition) {
            case "<":
                return criteriaBuilder.lt(root.get(macronutrient), value);
            case ">":
                return criteriaBuilder.gt(root.get(macronutrient), value);
            case "=":
                return criteriaBuilder.equal(root.get(macronutrient), value);
            default:
                throw new IllegalArgumentException("Unsupported condition: " + condition);
        }
    }

    private static Predicate combinePredicatesWithOperators(List<Predicate> predicates, List<String> operators, CriteriaBuilder criteriaBuilder) {
        if (predicates.isEmpty()) {
            return null;
        } else {
            Predicate combined = predicates.get(0);

            for (int i = 0; i < operators.size(); i++) {
                String operator = operators.get(i);
                Predicate nextPredicate = predicates.get(i + 1);

                if ("AND".equalsIgnoreCase(operator)) {
                    combined = criteriaBuilder.and(combined, nextPredicate);
                } else if ("OR".equalsIgnoreCase(operator)) {
                    combined = criteriaBuilder.or(combined, nextPredicate);
                } else {
                    throw new IllegalArgumentException("Unsupported logic condition: " + operator);
                }
            }

            return combined;
        }
    }
}
