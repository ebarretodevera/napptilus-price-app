package com.napptilus.napptiluspriceapp.filter;

import com.napptilus.napptiluspriceapp.exception.FilterException;
import com.napptilus.napptiluspriceapp.model.Prices;
import com.napptilus.napptiluspriceapp.repository.PricesRepository;
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
        if (filters.size() > 0) {
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
                        return (root, query, criteriaBuilder) -> {
                            if (noJoins(input))
                                return criteriaBuilder.like(root.get(input.getField()).as(String.class), "%" + input.getValue() + "%");
                            else
                                return criteriaBuilder.like(rootAndJoins(input, root).get(input.getField()).as(String.class), "%" + input.getValue() + "%");
                        };
                    case EQUALS:
                        return (root, query, criteriaBuilder) -> {
                            if (noJoins(input))
                                return criteriaBuilder.equal(root.get(input.getField()),
                                        castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
                            else
                                return criteriaBuilder.equal(root.get(input.getField()),
                                        castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValue()));
                        };
                    case NOT_EQ:
                        return (root, query, criteriaBuilder) -> {
                            if (noJoins(input))
                                return criteriaBuilder.notEqual(root.get(input.getField()),
                                        castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
                            else
                                return criteriaBuilder.notEqual(root.get(input.getField()),
                                        castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValue()));
                        };
                    case GREATER_THAN:
                        return (root, query, criteriaBuilder) -> {
                            if (noJoins(input))
                                return criteriaBuilder.gt(root.get(input.getField()),
                                        (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
                            else
                                return criteriaBuilder.gt(root.get(input.getField()),
                                        (Number) castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValue()));
                        };
                    case LESS_THAN:
                        return (root, query, criteriaBuilder) -> {
                            if (noJoins(input))
                                return criteriaBuilder.lt(root.get(input.getField()),
                                        (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
                            else
                                return criteriaBuilder.lt(root.get(input.getField()),
                                        (Number) castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValue()));
                        };
                    case IN:
                        return (root, query, criteriaBuilder) -> {
                            if (noJoins(input))
                                return criteriaBuilder.in(root.get(input.getField()))
                                        .value(castToRequiredType(root.get(input.getField()).getJavaType(), input.getValues()));
                            else
                                return criteriaBuilder.in(root.get(input.getField()))
                                        .value(castToRequiredType(rootAndJoins(input, root).get(input.getField()).getJavaType(), input.getValues()));
                        };
                    default:
                        throw new FilterException("Operation not supported yet");
                }
            case DATE:
                switch (input.getOperator()) {
                    case GREATER_THAN_OR_EQUALS:
                        return (root, query, criteriaBuilder) -> {
                            if (noJoins(input))
                                return criteriaBuilder.greaterThanOrEqualTo(root.get(input.getField()), input.getDateValue());
                            else
                                return criteriaBuilder.greaterThanOrEqualTo(rootAndJoins(input, root).get(input.getField()), input.getDateValue());
                        };
                    case LESS_THAN_OR_EQUALS:
                        return (root, query, criteriaBuilder) -> {
                            if (noJoins(input))
                                return criteriaBuilder.lessThanOrEqualTo(root.get(input.getField()), input.getDateValue());
                            else
                                return criteriaBuilder.lessThanOrEqualTo(rootAndJoins(input, root).get(input.getField()), input.getDateValue());
                        };
                    default:
                        throw new FilterException("Operation not supported yet");
                }
            default:
                throw new FilterException("Filter value type not supported yet");
        }
    }

    private boolean noJoins(Filter input) {
        return Objects.isNull(input.getTables()) || input.getTables().isEmpty();
    }

    private Join<Object, Object> rootAndJoins(Filter input, Root<Prices> root) {
        Join<Object, Object> join = null;
        for (String table : input.getTables()) {
            join = root.join(table);
        }
        return join;
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
