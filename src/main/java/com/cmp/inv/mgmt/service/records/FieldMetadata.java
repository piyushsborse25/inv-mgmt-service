package com.cmp.inv.mgmt.service.records;

import com.cmp.inv.mgmt.service.enums.FieldType;
import com.cmp.inv.mgmt.service.enums.SearchOperator;
import lombok.Builder;

import java.util.LinkedList;
import java.util.Set;

@Builder
public record FieldMetadata(
        String field,
        String jpaPath,
        Class<?> entityClass,
        Class<?> rootEntityClass,
        LinkedList<String> joinPath,
        String attribute,
        FieldType fieldType,
        Set<SearchOperator> allowedOperators
) {}

