package com.napptilus.napptiluspriceapp.filter;

import com.napptilus.napptiluspriceapp.exception.FilterException;
import com.napptilus.napptiluspriceapp.model.Prices;
import com.napptilus.napptiluspriceapp.repository.PricesRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specification.where;

@Slf4j
@Component
public class PricesFilter {

    @Autowired
    private PricesRepository pricesRepository;

    public List<Prices> query(List<Filter> filters, List<Sort.Order> orders) throws FilterException {
        if (!filters.isEmpty()) {
            return pricesRepository.findAll(getSpecificationFromFilters(filters), Sort.by(orders));
        } else {
            return pricesRepository.findAll(Sort.by(orders));
        }
    }

    private Specification<Prices> getSpecificationFromFilters(List<Filter> filter) throws FilterException {
        Specification<Prices> specification = where(createSpecification(filter.remove(0)));
        for (Filter input : filter) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private Specification<Prices> createSpecification(Filter input) throws FilterException {
        switch (input.getValueType()) {
            case STRING:
                switch (input.getOperator()) {
                    case LIKE:
                        return (root, query, criteriaBuilder) -> like(input, root, criteriaBuilder);
                    case EQUALS:
                        return (root, query, criteriaBuilder) -> equals(input, root, criteriaBuilder);
                    case NOT_EQ:
                        return (root, query, criteriaBuilder) -> notEquals(input, root, criteriaBuilder);
                    case GREATER_THAN:
                        return (root, query, criteriaBuilder) -> greaterThan(input, root, criteriaBuilder);
                    case LESS_THAN:
                        return (root, query, criteriaBuilder) -> lessThan(input, root, criteriaBuilder);
                    case IN:
                        return (root, query, criteriaBuilder) -> in(input, root, criteriaBuilder);
                    default:
                        throw new FilterException("Operation not supported yet");
                }
            case DATE:
                switch (input.getOperator()) {
                    case GREATER_THAN_OR_EQUALS:
                        return (root, query, criteriaBuilder) -> dateGreaterThanOrEquals(input, root, criteriaBuilder);
                    case LESS_THAN_OR_EQUALS:
                        return (root, query, criteriaBuilder) -> dateLessThanOrEquals(input, root, criteriaBuilder);
                    default:
                        throw new FilterException("Operation not supported yet");
                }
            default:
                throw new FilterException("Filter value type not supported yet");
        }
    }

    private Predicate dateLessThanOrEquals(Filter input, Root<Prices> root, CriteriaBuilder criteriaBuilder) {
        if (noJoins(input))
            return criteriaBuilder.lessThanOrEqualTo(root.get(input.getField()), input.getDateValue());
        else
            return criteriaBuilder.lessThanOrEqualTo(rootAndJoins(input, root).get(input.getField()), input.getDateValue());
    }

    private Predicate dateGreaterThanOrEquals(Filter input, Root<Prices> root, CriteriaBuilder criteriaBuilder) {
        if (noJoins(input))
            return criteriaBuilder.greaterThanOrEqualTo(root.get(input.getField()), input.getDateValue());
        else
            return criteriaBuilder.greaterThanOrEqualTo(rootAndJoins(input, root).get(input.getField()), input.getDateValue());
    }

    private CriteriaBuilder.In<Object> in(Filter input, Root<Prices> root, CriteriaBuilder criteriaBuilder) {
        if (noJoins(input))
            return criteriaBuilder.in(root.get(input.getField()))
                    .value(castToRequiredType(root.get(input.getField()).getJavaType(), input.getValues()));
        else
            return criteriaBuilder.in(root.get(input.getField()))
                    .value(castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValues()));
    }

    private Predicate lessThan(Filter input, Root<Prices> root, CriteriaBuilder criteriaBuilder) {
        if (noJoins(input))
            return criteriaBuilder.lt(root.get(input.getField()),
                    (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
        else
            return criteriaBuilder.lt(root.get(input.getField()),
                    (Number) castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValue()));
    }

    private Predicate greaterThan(Filter input, Root<Prices> root, CriteriaBuilder criteriaBuilder) {
        if (noJoins(input))
            return criteriaBuilder.gt(root.get(input.getField()),
                    (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
        else
            return criteriaBuilder.gt(root.get(input.getField()),
                    (Number) castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValue()));
    }

    private Predicate notEquals(Filter input, Root<Prices> root, CriteriaBuilder criteriaBuilder) {
        if (noJoins(input))
            return criteriaBuilder.notEqual(root.get(input.getField()),
                    castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
        else
            return criteriaBuilder.notEqual(root.get(input.getField()),
                    castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValue()));
    }

    private Predicate equals(Filter input, Root<Prices> root, CriteriaBuilder criteriaBuilder) {
        if (noJoins(input))
            return criteriaBuilder.equal(root.get(input.getField()),
                    castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
        else
            return criteriaBuilder.equal(root.get(input.getField()),
                    castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValue()));
    }

    private Predicate like(Filter input, Root<Prices> root, CriteriaBuilder criteriaBuilder) {
        if (noJoins(input))
            return criteriaBuilder.like(root.get(input.getField()).as(String.class), "%" + input.getValue() + "%");
        else
            return criteriaBuilder.like(rootAndJoins(input, root).get(input.getField()).as(String.class), "%" + input.getValue() + "%");
    }

    private boolean noJoins(Filter input) {
        return Objects.isNull(input.getTables()) || input.getTables().isEmpty();
    }

    @NonNull
    private Join<Object, Object> rootAndJoins(Filter input, Root<Prices> root) {
        Join<Object, Object> join = null;
        for (String table : input.getTables()) {
            join = root.join(table);
        }
        return Objects.requireNonNull(join);
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (fieldType.isAssignableFrom(Long.class)) {
            return Long.valueOf(value);
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, value);
        }
        return null;
    }

    private Object castToRequiredType(Class fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }
}
