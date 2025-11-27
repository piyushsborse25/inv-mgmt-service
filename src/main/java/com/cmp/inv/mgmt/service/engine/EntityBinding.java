package com.cmp.inv.mgmt.service.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityBinding {
    private final Class<?> entityClass;
    private final String basePathFromOrder;
    private final int depth;
}
