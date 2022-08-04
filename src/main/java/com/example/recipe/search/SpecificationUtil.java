package com.example.recipe.search;

import com.example.recipe.config.SqlFunctionsRegistry;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


public class SpecificationUtil {
    public static <T> Specification<T> bySearchQuery(SearchQuery searchQuery) {

        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            List<JoinColumnProps> joinColumnProps = searchQuery.getJoinColumnProps();

            if (joinColumnProps != null && !joinColumnProps.isEmpty()) {
                for (JoinColumnProps joinColumnProp : joinColumnProps) {
                    addJoinColumnProps(predicates, joinColumnProp, criteriaBuilder, root);
                }
            }

            List<SearchFilter> searchFilters = searchQuery.getSearchFilters();

            if (searchFilters != null && !searchFilters.isEmpty()) {

                for (final SearchFilter searchFilter : searchFilters) {
                    addPredicates(predicates, searchFilter, criteriaBuilder, root);
                }
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    private static <T> void addJoinColumnProps(List<Predicate> predicates, JoinColumnProps joinColumnProp,
                                               CriteriaBuilder criteriaBuilder, Root<T> root) {

        SearchFilter searchFilter = joinColumnProp.getSearchFilter();
        Join<Object, Object> joinParent = root.join(joinColumnProp.getJoinColumnName());

        String property = searchFilter.getColumnName();
        Path expression = joinParent.get(property);

        addPredicate(predicates, searchFilter, criteriaBuilder, expression);

    }

    private static <T> void addPredicates(List<Predicate> predicates, SearchFilter searchFilter,
                                          CriteriaBuilder criteriaBuilder, Root<T> root) {
        String property = searchFilter.getColumnName();
        Path expression = root.get(property);

        addPredicate(predicates, searchFilter, criteriaBuilder, expression);

    }

    private static void addPredicate(List<Predicate> predicates, SearchFilter searchFilter,
                                     CriteriaBuilder criteriaBuilder, Path expression) {
        switch (searchFilter.getOperator()) {
            case EQUALS:
                predicates.add(criteriaBuilder.equal(expression, searchFilter.getValue()));
                break;
            case LIKE:
                predicates.add(criteriaBuilder.like(expression, "%" + searchFilter.getValue() + "%"));
                break;
            case IN:
                predicates.add(criteriaBuilder.in(expression).value(searchFilter.getValue()));
                break;
            case GREATER_THAN:
                predicates.add(criteriaBuilder.greaterThan(expression, (Comparable) searchFilter.getValue()));
                break;
            case LESS_THAN:
                predicates.add(criteriaBuilder.lessThan(expression, (Comparable) searchFilter.getValue()));
                break;
            case GREATER_THAN_OR_EQUAL_TO:
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) searchFilter.getValue()));
                break;
            case LESS_THAN_OR_EQUAL_TO:
                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) searchFilter.getValue()));
                break;
            case NOT_EQ:
                predicates.add(criteriaBuilder.notEqual(expression, searchFilter.getValue()));
                break;
            case IS_NULL:
                predicates.add(criteriaBuilder.isNull(expression));
                break;
            case IS_NOT_NULL:
                predicates.add(criteriaBuilder.isNotNull(expression));
                break;
            case FULL_TEXT_SEARCH:
                predicates.add(criteriaBuilder.equal(criteriaBuilder
                        .function(SqlFunctionsRegistry.FUNCTION_FULL_TEXT_SEARCH
                                , String.class
                                , expression, criteriaBuilder.literal(searchFilter.getValue())), true));
                break;
            default:
                System.out.println("Predicate is not matched");
                throw new IllegalArgumentException(searchFilter.getOperator() + " is not a valid predicate");
        }

    }
}