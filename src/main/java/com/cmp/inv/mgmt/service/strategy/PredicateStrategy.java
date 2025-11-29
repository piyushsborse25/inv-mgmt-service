package com.cmp.inv.mgmt.service.strategy;

import com.cmp.inv.mgmt.service.enums.SearchOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public interface PredicateStrategy {
    Predicate build(SearchOperator op, CriteriaBuilder cb, Path<?> path, Object value);
}
