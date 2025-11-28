package com.cmp.inv.mgmt.service.records;

import com.cmp.inv.mgmt.service.enums.FieldType;
import com.cmp.inv.mgmt.service.enums.SearchOperator;
import lombok.Builder;

import java.util.Set;

@Builder
public record FieldMetadata(String field, Class<?> entityClass, String jpaPath, FieldType fieldType, int depth,
                            Set<SearchOperator> allowedOperators) {
}
