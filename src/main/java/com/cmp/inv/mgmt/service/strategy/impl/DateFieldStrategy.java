package com.cmp.inv.mgmt.service.strategy.impl;

import com.cmp.inv.mgmt.service.enums.SearchOperator;
import com.cmp.inv.mgmt.service.strategy.PredicateStrategy;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DateFieldStrategy implements PredicateStrategy {

    @Override
    public Predicate build(SearchOperator op, CriteriaBuilder cb, Path<?> path, Object value) {
        LocalDateTime dateVal = toDate(value);

        return switch (op) {
            case EQUALS, ON -> cb.equal(path.as(LocalDateTime.class), dateVal);
            case NOT_EQUALS -> cb.notEqual(path.as(LocalDateTime.class), dateVal);
            case BEFORE, LESS_THAN -> cb.lessThan(path.as(LocalDateTime.class), dateVal);
            case AFTER, GREATER_THAN -> cb.greaterThan(path.as(LocalDateTime.class), dateVal);
            case GREATER_THAN_OR_EQUAL -> cb.greaterThanOrEqualTo(path.as(LocalDateTime.class), dateVal);
            case LESS_THAN_OR_EQUAL -> cb.lessThanOrEqualTo(path.as(LocalDateTime.class), dateVal);
            case BETWEEN -> {
                List<?> dates = castList(value);
                if (dates.size() != 2) throw new IllegalArgumentException("BETWEEN requires [start, end] dates");

                LocalDateTime start = toDate(dates.get(0));
                LocalDateTime end = toDate(dates.get(1));

                yield cb.between(path.as(LocalDateTime.class), start, end);
            }
            case IS_NULL -> cb.isNull(path);
            case IS_NOT_NULL -> cb.isNotNull(path);
            default -> throw new UnsupportedOperationException("Operator " + op + " not supported for DATE field");
        };
    }

    private LocalDateTime toDate(Object value) {
        if (value instanceof LocalDateTime dt) return dt;
        if (value instanceof LocalDate d) return d.atStartOfDay();

        if (value instanceof String s) {
            try {
                return LocalDateTime.parse(s); // ISO-8601: "2024-12-30T10:00:00"
            } catch (Exception e1) {
                try {
                    return LocalDate.parse(s).atStartOfDay(); // "2024-12-30"
                } catch (Exception e2) {
                    throw new IllegalArgumentException("Invalid date format: " + s);
                }
            }
        }

        throw new IllegalArgumentException("Date value must be LocalDate/LocalDateTime/String");
    }

    private List<?> castList(Object value) {
        if (!(value instanceof List<?> list))
            throw new IllegalArgumentException("BETWEEN requires a List of two Dates");
        return list;
    }
}
