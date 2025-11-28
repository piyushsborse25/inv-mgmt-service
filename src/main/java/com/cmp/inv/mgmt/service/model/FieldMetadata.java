package com.cmp.inv.mgmt.service.model;

import com.cmp.inv.mgmt.service.enums.FieldType;
import com.cmp.inv.mgmt.service.enums.SearchField;
import com.cmp.inv.mgmt.service.enums.SearchOperator;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@Builder
@ToString
public class FieldMetadata {
    private final SearchField field;
    private final Class<?> entityClass;
    private final String jpaPath;
    private final FieldType fieldType;
    private final int depth;
    private final Set<SearchOperator> allowedOperators;
}
