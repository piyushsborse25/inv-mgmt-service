package com.cmp.inv.mgmt.service.utils;

import com.cmp.inv.mgmt.service.enums.FieldType;
import com.cmp.inv.mgmt.service.enums.SearchOperator;

import java.util.EnumSet;
import java.util.Set;

public final class AllowedOperators {

    private AllowedOperators() {
    }

    public static Set<SearchOperator> forType(FieldType type) {
        return switch (type) {
            case STRING -> EnumSet.of(
                    SearchOperator.EQUALS,
                    SearchOperator.NOT_EQUALS,
                    SearchOperator.CONTAINS,
                    SearchOperator.NOT_CONTAINS,
                    SearchOperator.STARTS_WITH,
                    SearchOperator.ENDS_WITH,
                    SearchOperator.IN,
                    SearchOperator.NOT_IN,
                    SearchOperator.IS_NULL,
                    SearchOperator.IS_NOT_NULL
            );
            case NUMBER -> EnumSet.of(
                    SearchOperator.EQUALS,
                    SearchOperator.NOT_EQUALS,
                    SearchOperator.GREATER_THAN,
                    SearchOperator.GREATER_THAN_OR_EQUAL,
                    SearchOperator.LESS_THAN,
                    SearchOperator.LESS_THAN_OR_EQUAL,
                    SearchOperator.BETWEEN,
                    SearchOperator.IN,
                    SearchOperator.NOT_IN,
                    SearchOperator.IS_NULL,
                    SearchOperator.IS_NOT_NULL
            );
            case DATE -> EnumSet.of(
                    SearchOperator.BEFORE,
                    SearchOperator.AFTER,
                    SearchOperator.BETWEEN,
                    SearchOperator.ON,
                    SearchOperator.IS_NULL,
                    SearchOperator.IS_NOT_NULL
            );
            case ENUM -> EnumSet.of(
                    SearchOperator.EQUALS,
                    SearchOperator.NOT_EQUALS,
                    SearchOperator.IN,
                    SearchOperator.NOT_IN,
                    SearchOperator.IS_NULL,
                    SearchOperator.IS_NOT_NULL
            );
            case BOOLEAN -> EnumSet.of(
                    SearchOperator.EQUALS,
                    SearchOperator.NOT_EQUALS,
                    SearchOperator.IS_NULL,
                    SearchOperator.IS_NOT_NULL
            );
            case RELATION -> EnumSet.of(
                    SearchOperator.EQUALS,
                    SearchOperator.IN,
                    SearchOperator.NOT_IN,
                    SearchOperator.IS_NULL,
                    SearchOperator.IS_NOT_NULL
            );
        };
    }
}
