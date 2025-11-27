package com.cmp.inv.mgmt.service.engine;

import com.cmp.inv.mgmt.service.enums.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class FieldTypeResolver {

    private FieldTypeResolver() {}

    public static FieldType fromJavaType(Class<?> type) {
        if (String.class.equals(type)) {
            return FieldType.STRING;
        }
        if (Number.class.isAssignableFrom(type) || BigDecimal.class.equals(type)
                || type.equals(Integer.class) || type.equals(Long.class)
                || type.equals(Double.class)) {
            return FieldType.NUMBER;
        }
        if (LocalDate.class.equals(type) || LocalDateTime.class.equals(type)) {
            return FieldType.DATE;
        }
        if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return FieldType.BOOLEAN;
        }
        if (type.isEnum()) {
            return FieldType.ENUM;
        }
        // fallback for relations (Customer, Product, Category, etc.)
        return FieldType.RELATION;
    }
}
