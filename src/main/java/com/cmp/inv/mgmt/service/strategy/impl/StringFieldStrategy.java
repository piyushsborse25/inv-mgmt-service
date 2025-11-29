package com.cmp.inv.mgmt.service.strategy.impl;

import com.cmp.inv.mgmt.service.enums.SearchOperator;
import com.cmp.inv.mgmt.service.strategy.PredicateStrategy;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class StringFieldStrategy implements PredicateStrategy {

    @Override
    public Predicate build(SearchOperator op, CriteriaBuilder cb, Path<?> path, Object value) {
        String val = value != null ? value.toString().toLowerCase() : null;

        return switch (op) {
            case EQUALS -> cb.equal(cb.lower(path.as(String.class)), val);
            case NOT_EQUALS -> cb.notEqual(cb.lower(path.as(String.class)), val);
            case CONTAINS -> cb.like(cb.lower(path.as(String.class)), "%" + val + "%");
            case NOT_CONTAINS -> cb.notLike(cb.lower(path.as(String.class)), "%" + val + "%");
            case STARTS_WITH -> cb.like(cb.lower(path.as(String.class)), val + "%");
            case ENDS_WITH -> cb.like(cb.lower(path.as(String.class)), "%" + val);
            case IS_NULL -> cb.isNull(path);
            case IS_NOT_NULL -> cb.isNotNull(path);
            default -> throw new UnsupportedOperationException("Operator " + op + " not supported for STRING type");
        };
    }
}

