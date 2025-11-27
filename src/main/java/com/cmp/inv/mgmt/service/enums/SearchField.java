package com.cmp.inv.mgmt.service.enums;

import lombok.Getter;

@Getter
public enum SearchField {

    // 🔹 Customer Fields
    CUSTOMER_ID("id"),
    CUSTOMER_EMAIL("email"),
    CUSTOMER_FULL_NAME("fullName"),
    CUSTOMER_BILLING_ADDRESS("billingAddress"),
    CUSTOMER_DEFAULT_SHIPPING_ADDRESS("defaultShippingAddress"),
    CUSTOMER_COUNTRY("country"),
    CUSTOMER_PHONE("phone"),

    // 🔹 Product Fields
    PRODUCT_ID("id"),
    PRODUCT_SKU("sku"),
    PRODUCT_NAME("name"),
    PRODUCT_DESCRIPTION("description"),
    PRODUCT_PRICE("price"),
    PRODUCT_STOCK("stock"),
    PRODUCT_IMAGE_URL("imageUrl"),
    PRODUCT_CATEGORY("category"),

    // 🔹 Category Fields
    CATEGORY_ID("id"),
    CATEGORY_NAME("name"),
    CATEGORY_DESCRIPTION("description"),
    CATEGORY_CREATED_AT("createdAt"),
    CATEGORY_UPDATED_AT("updatedAt"),

    // 🔹 Order Fields
    ORDER_ID("id"),
    ORDER_TOTAL_PRICE("totalPrice"),
    ORDER_STATUS("status"),
    ORDER_CREATED_AT("createdAt"),
    ORDER_CUSTOMER("customer"),

    // 🔹 OrderDetail Fields (for filtering inside orders)
    ORDER_DETAIL_PRODUCT("product"),
    ORDER_DETAIL_QUANTITY("quantity"),
    ORDER_DETAIL_UNIT_PRICE("unitPrice");

    private final String fieldName;

    SearchField(String fieldName) {
        this.fieldName = fieldName;
    }
}
