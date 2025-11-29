package com.cmp.inv.mgmt.service.utils;

import com.cmp.inv.mgmt.service.InvMgmtServiceApplication;
import com.cmp.inv.mgmt.service.entities.Category;
import com.cmp.inv.mgmt.service.entities.Order;
import com.cmp.inv.mgmt.service.records.FieldMetadata;
import com.cmp.inv.mgmt.service.records.SearchKey;
import com.cmp.inv.mgmt.service.registry.SearchMetadataRegistry;
import com.cmp.inv.mgmt.service.repos.OrderRepository;
import jakarta.persistence.criteria.Path;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class Executor {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context = SpringApplication.run(InvMgmtServiceApplication.class, args);

        try (context) {
            SearchMetadataRegistry registry = context.getBean(SearchMetadataRegistry.class);
            OrderRepository orderRepository = context.getBean(OrderRepository.class);

            FieldMetadata metadata = registry.get(new SearchKey(Category.class, "name"));
            System.out.println("Metadata: " + metadata);

            Specification<Order> spec = (root, query, cb) -> {
                if (query != null) {
                    query.distinct(true);
                }
                Path<Object> path = root.join(metadata.joinPath().get(0))
                        .join(metadata.joinPath().get(1))
                        .join(metadata.joinPath().get(2))
                        .get(metadata.field());

                return cb.equal(path, "Computers");
            };

            List<Order> result = orderRepository.findAll(spec);
            result.forEach(System.out::println);
        }
    }
}
