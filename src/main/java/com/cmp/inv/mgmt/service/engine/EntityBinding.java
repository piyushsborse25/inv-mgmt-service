package com.cmp.inv.mgmt.service.engine;

public record EntityBinding(Class<?> entityClass, String basePathFromOrder, int depth) {
}
