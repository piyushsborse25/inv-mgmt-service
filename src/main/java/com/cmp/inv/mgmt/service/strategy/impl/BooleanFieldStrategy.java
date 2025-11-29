package com.cmp.inv.mgmt.service.strategy.impl;

import com.cmp.inv.mgmt.service.enums.SearchOperator;
import com.cmp.inv.mgmt.service.strategy.PredicateStrategy;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class BooleanFieldStrategy implements PredicateStrategy {

    @Override
    public Predicate build(SearchOperator op, CriteriaBuilder cb, Path<?> path, Object value) {

        return switch (op) {
            case EQUALS -> cb.equal(path.as(Boolean.class), toBool(value));
            case NOT_EQUALS -> cb.notEqual(path.as(Boolean.class), toBool(value));
            case IS_NULL -> cb.isNull(path);
            case IS_NOT_NULL -> cb.isNotNull(path);
            default -> throw new UnsupportedOperationException("Operator " + op + " not supported for BOOLEAN type");
        };
    }

    private Boolean toBool(Object value) {
        if (value instanceof Boolean b) return b;
        if (value instanceof String s) return Boolean.parseBoolean(s.trim());
        throw new IllegalArgumentException("Invalid Boolean value: " + value);
    }
}
