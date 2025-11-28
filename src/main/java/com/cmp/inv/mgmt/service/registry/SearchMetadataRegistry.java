package com.cmp.inv.mgmt.service.registry;

import com.cmp.inv.mgmt.service.annotations.SearchRoot;
import com.cmp.inv.mgmt.service.annotations.Searchable;
import com.cmp.inv.mgmt.service.engine.AllowedOperators;
import com.cmp.inv.mgmt.service.engine.FieldTypeResolver;
import com.cmp.inv.mgmt.service.enums.FieldType;
import com.cmp.inv.mgmt.service.enums.SearchOperator;
import com.cmp.inv.mgmt.service.records.FieldMetadata;
import com.cmp.inv.mgmt.service.records.SearchKey;
import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class SearchMetadataRegistry {

    private final Map<SearchKey, FieldMetadata> metadataMap = new HashMap<>();

    @Value("${search.entities.base-package}")
    private String basePackage;

    @PostConstruct
    public void init() {
        System.out.println("Initializing Search Metadata Registry...");

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> searchRootClasses =
                reflections.getTypesAnnotatedWith(SearchRoot.class);

        for (Class<?> entityClass : searchRootClasses) {
            SearchRoot rootAnn = entityClass.getAnnotation(SearchRoot.class);
            String basePath = rootAnn.path();
            for (Field field : entityClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Searchable.class)) {
                    continue;
                }
                registerField(entityClass, field, basePath);
            }
        }

        System.out.println("Search Metadata Ready: " + metadataMap.size() + " fields registered.");
    }

    private void registerField(Class<?> entityClass, Field field, String basePath) {

        String jpaPath = basePath + "." + field.getName();
        jpaPath = jpaPath.toLowerCase();
        FieldType fieldType = FieldTypeResolver.fromJavaType(field.getType());
        Set<SearchOperator> allowedOperators = AllowedOperators.forType(fieldType);
        int depth = jpaPath.split("\\.").length;

        SearchKey key = new SearchKey(entityClass, field.getName());

        FieldMetadata metadata = FieldMetadata.builder()
                .entityClass(entityClass)
                .field(field.getName())
                .jpaPath(jpaPath)
                .fieldType(fieldType)
                .depth(depth)
                .allowedOperators(allowedOperators)
                .build();

        metadataMap.put(key, metadata);

        System.out.println("Registered: " + key + " -> " + jpaPath);
    }

    public FieldMetadata get(SearchKey key) {
        System.out.println(metadataMap.values());
        return metadataMap.get(key);
    }

    public Map<SearchKey, FieldMetadata> getAll() {
        return Collections.unmodifiableMap(metadataMap);
    }
}
