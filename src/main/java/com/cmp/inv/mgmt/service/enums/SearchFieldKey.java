package com.cmp.inv.mgmt.service.enums;

public enum SearchFieldKey {

    // -------- Order --------
    ORDER_ID("order.id"),
    ORDER_CREATED_AT("order.createdat"),
    ORDER_TOTAL_PRICE("order.totalprice"),

    // -------- Customer --------
    CUSTOMER_ID("customer.id"),
    CUSTOMER_EMAIL("customer.email"),
    CUSTOMER_PHONE("customer.phone"),
    CUSTOMER_FULL_NAME("customer.fullname"),
    CUSTOMER_COUNTRY("customer.country"),

    // -------- Order Detail --------
    ORDER_DETAIL_ID("orderdetail.id"),
    ORDER_DETAIL_QUANTITY("orderdetail.quantity"),

    // -------- Product --------
    PRODUCT_ID("product.id"),
    PRODUCT_SKU("product.sku"),
    PRODUCT_NAME("product.name"),
    PRODUCT_DESCRIPTION("product.description"),
    PRODUCT_PRICE("product.price"),
    PRODUCT_STOCK("product.stock"),

    // -------- Category --------
    CATEGORY_ID("category.id"),
    CATEGORY_NAME("category.name"),
    CATEGORY_CREATED_AT("category.createdat"),
    CATEGORY_UPDATED_AT("category.updatedat");

    private final String searchKey;

    SearchFieldKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String toField() {
        return searchKey;
    }
}