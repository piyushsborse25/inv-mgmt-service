package com.cmp.inv.mgmt.service.strategy.impl;

import com.cmp.inv.mgmt.service.enums.SearchOperator;
import com.cmp.inv.mgmt.service.strategy.PredicateStrategy;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.Collection;
import java.util.List;

public class NumberFieldStrategy implements PredicateStrategy {

    @Override
    public Predicate build(SearchOperator op,
                           CriteriaBuilder cb,
                           Path<?> path,
                           Object value) {

        return switch (op) {
            case EQUALS ->
                    cb.equal(path, convert(value));
            case NOT_EQUALS ->
                    cb.notEqual(path, convert(value));
            case GREATER_THAN ->
                    cb.greaterThan(
                            path.as(Double.class),
                            convert(value)
                    );
            case GREATER_THAN_OR_EQUAL ->
                    cb.greaterThanOrEqualTo(
                            path.as(Double.class),
                            convert(value)
                    );
            case LESS_THAN ->
                    cb.lessThan(
                            path.as(Double.class),
                            convert(value)
                    );
            case LESS_THAN_OR_EQUAL ->
                    cb.lessThanOrEqualTo(
                            path.as(Double.class),
                            convert(value)
                    );
            case BETWEEN -> {
                List<?> values = castList(value);
                if (values.size() != 2)
                    throw new IllegalArgumentException("BETWEEN requires [min, max] list");

                Double min = convert(values.get(0));
                Double max = convert(values.get(1));

                yield cb.between(path.as(Double.class), min, max);
            }
            case IN -> {
                Collection<?> collection = castCollection(value);
                yield path.in(collection);
            }
            case NOT_IN -> {
                Collection<?> collection = castCollection(value);
                yield cb.not(path.in(collection));
            }
            case IS_NULL -> cb.isNull(path);
            case IS_NOT_NULL -> cb.isNotNull(path);
            default ->
            throw new UnsupportedOperationException(
                    "Operator " + op + " not supported for NUMBER type"
            );
        };
    }

    private Double convert(Object value) {
        if (!(value instanceof Number n))
            throw new IllegalArgumentException("Value must be numeric");
        return n.doubleValue();
    }

    private List<?> castList(Object value) {
        if (!(value instanceof List<?> list))
            throw new IllegalArgumentException("BETWEEN requires a List of numbers");
        return list;
    }

    private Collection<?> castCollection(Object value) {
        if (!(value instanceof Collection<?> col))
            throw new IllegalArgumentException("IN/NOT_IN requires a Collection");
        return col;
    }
}