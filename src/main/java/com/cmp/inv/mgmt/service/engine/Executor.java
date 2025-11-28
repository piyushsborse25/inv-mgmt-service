package com.cmp.inv.mgmt.service.engine;

import com.cmp.inv.mgmt.service.InvMgmtServiceApplication;
import com.cmp.inv.mgmt.service.entities.Category;
import com.cmp.inv.mgmt.service.records.FieldMetadata;
import com.cmp.inv.mgmt.service.records.SearchKey;
import com.cmp.inv.mgmt.service.registry.SearchMetadataRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class Executor {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context = SpringApplication.run(InvMgmtServiceApplication.class, args);

        try (context) {
            SearchMetadataRegistry registry = context.getBean(SearchMetadataRegistry.class);
            FieldMetadata metadata = registry.get(new SearchKey(Category.class, "name"));
            System.out.println("Metadata: " + metadata);
        }
    }
}
