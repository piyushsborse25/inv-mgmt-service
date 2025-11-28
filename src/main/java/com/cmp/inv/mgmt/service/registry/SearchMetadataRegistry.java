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
import java.util.*;

@Component
public class SearchMetadataRegistry {

    private final Map<SearchKey, FieldMetadata> metadataMap = new HashMap<>();

    @Value("${search.entities.base-package}")
    private String basePackage;

    @Value("${search.entities.root-class}")
    private String rootClass;

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
                if (!field.isAnnotationPresent(Searchable.class)) continue;
                try {
                    registerField(entityClass, field, basePath);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println("Search Metadata Ready: " + metadataMap.size() + " fields registered.");
    }

    private void registerField(Class<?> entityClass, Field field, String basePath) throws ClassNotFoundException {

        LinkedList<String> joinPath =
                basePath == null || basePath.isBlank()
                        ? new LinkedList<>()
                        : new LinkedList<>(Arrays.stream(basePath.toLowerCase().split("\\.")).skip(1).toList());

        FieldType fieldType = FieldTypeResolver.fromJavaType(field.getType());
        Set<SearchOperator> allowedOperators = AllowedOperators.forType(fieldType);

        String attribute = field.getName();
        String jpaPath = basePath + "." + attribute;
        SearchKey key = new SearchKey(entityClass, attribute);


        FieldMetadata metadata = FieldMetadata.builder()
                .field(attribute)
                .jpaPath(jpaPath.toLowerCase())
                .entityClass(entityClass)
                .rootEntityClass(Class.forName(rootClass))
                .joinPath(joinPath)
                .attribute(attribute)
                .fieldType(fieldType)
                .allowedOperators(allowedOperators)
                .build();

        metadataMap.put(key, metadata);

        System.out.println(
                "Registered: entity=" + entityClass.getSimpleName() +
                        " attr=" + attribute +
                        " path=" + jpaPath +
                        " joins=" + joinPath);
    }

    public FieldMetadata get(SearchKey key) {
        return metadataMap.get(key);
    }

    public Map<SearchKey, FieldMetadata> getAll() {
        return Collections.unmodifiableMap(metadataMap);
    }
}
