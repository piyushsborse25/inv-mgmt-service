package com.cmp.inv.mgmt.service.records;

public record EntityBinding(Class<?> entityClass, String basePathFromOrder, int depth) {
}
