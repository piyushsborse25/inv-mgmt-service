package com.cmp.inv.mgmt.service.registry.impl;

import com.cmp.inv.mgmt.service.engine.AllowedOperators;
import com.cmp.inv.mgmt.service.engine.EntityBinding;
import com.cmp.inv.mgmt.service.engine.FieldTypeResolver;
import com.cmp.inv.mgmt.service.entities.*;
import com.cmp.inv.mgmt.service.enums.FieldType;
import com.cmp.inv.mgmt.service.enums.SearchField;
import com.cmp.inv.mgmt.service.enums.SearchOperator;
import com.cmp.inv.mgmt.service.model.FieldMetadata;
import com.cmp.inv.mgmt.service.registry.FieldMetadataRegistry;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class FieldMetadataRegistryImpl implements FieldMetadataRegistry {

    private final Map<SearchField, FieldMetadata> metadataMap = new EnumMap<>(SearchField.class);

    // Business config: which entity + base path + depth for each SearchField
    private final Map<SearchField, EntityBinding> bindings = new EnumMap<>(SearchField.class);

    public FieldMetadataRegistryImpl() {
        initBindings();      // business mappings
        buildMetadata();     // reflection + rules
    }

    @Override
    public FieldMetadata get(SearchField field) {
        return metadataMap.get(field);
    }

    // Business bindings (who owns what)
    private void initBindings() {
        // ===== CUSTOMER (depth=1) =====
        bindings.put(SearchField.CUSTOMER_FULL_NAME,
                new EntityBinding(Customer.class, "customer", 1));
        bindings.put(SearchField.CUSTOMER_COUNTRY,
                new EntityBinding(Customer.class, "customer", 1));
        bindings.put(SearchField.CUSTOMER_EMAIL,
                new EntityBinding(Customer.class, "customer", 1));

        // ===== ORDER (depth=2, root) =====
        bindings.put(SearchField.ORDER_STATUS,
                new EntityBinding(Order.class, "", 2));
        bindings.put(SearchField.ORDER_CREATED_AT,
                new EntityBinding(Order.class, "", 2));
        bindings.put(SearchField.ORDER_TOTAL_PRICE,
                new EntityBinding(Order.class, "", 2));

        // ===== ORDER DETAIL (depth=3) =====
        bindings.put(SearchField.ORDER_DETAIL_QUANTITY,
                new EntityBinding(OrderDetail.class, "orderDetails", 3));
        bindings.put(SearchField.ORDER_DETAIL_UNIT_PRICE,
                new EntityBinding(OrderDetail.class, "orderDetails", 3));

        // ===== PRODUCT (depth=3) =====
        bindings.put(SearchField.PRODUCT_NAME,
                new EntityBinding(Product.class, "orderDetails.product", 3));
        bindings.put(SearchField.PRODUCT_PRICE,
                new EntityBinding(Product.class, "orderDetails.product", 3));
        bindings.put(SearchField.PRODUCT_STOCK,
                new EntityBinding(Product.class, "orderDetails.product", 3));
        bindings.put(SearchField.PRODUCT_IMAGE_URL,
                new EntityBinding(Product.class, "orderDetails.product", 3));

        // ===== CATEGORY (depth=4) =====
        bindings.put(SearchField.CATEGORY_NAME,
                new EntityBinding(Category.class, "orderDetails.product.category", 4));
        bindings.put(SearchField.CATEGORY_DESCRIPTION,
                new EntityBinding(Category.class, "orderDetails.product.category", 4));
        // createdAt/updatedAt if needed...
    }

    // Build metadata using Reflection + business rules
    private void buildMetadata() {
        for (SearchField sf : SearchField.values()) {

            EntityBinding binding = bindings.get(sf);
            if (binding == null) {
                continue;
            }

            Class<?> entityClass = binding.getEntityClass();
            String javaFieldName = sf.getFieldName();

            Field javaField;
            try {
                javaField = entityClass.getDeclaredField(javaFieldName);
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException("Invalid SearchField mapping. Field '" +
                        javaFieldName + "' not found on " + entityClass.getSimpleName(), e);
            }

            FieldType fieldType = FieldTypeResolver.fromJavaType(javaField.getType());
            Set<SearchOperator> allowedOps = AllowedOperators.forType(fieldType);

            String jpaPath;
            if (binding.getBasePathFromOrder() == null || binding.getBasePathFromOrder().isBlank()) {
                // Order root field, no prefix
                jpaPath = javaFieldName;
            } else {
                jpaPath = binding.getBasePathFromOrder() + "." + javaFieldName;
            }

            FieldMetadata metadata = FieldMetadata.builder()
                    .field(sf)
                    .entityClass(entityClass)
                    .jpaPath(jpaPath)
                    .fieldType(fieldType)
                    .depth(binding.getDepth())
                    .allowedOperators(allowedOps)
                    .build();

            metadataMap.put(sf, metadata);
        }
    }
}
