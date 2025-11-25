package com.cmp.inv.mgmt.service.init;

import com.cmp.inv.mgmt.service.entities.Category;
import com.cmp.inv.mgmt.service.entities.Product;
import com.cmp.inv.mgmt.service.repos.CategoryRepository;
import com.cmp.inv.mgmt.service.repos.ProductRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DBInitializer implements CommandLineRunner {

    private static final Logger log = LogManager.getLogger(DBInitializer.class);
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final Faker faker = new Faker(new Locale("en-IND"));
    private final Random random = new Random();

    @Override
    public void run(String... args) {

        if (productRepository.count() > 0) {
            return; // DB already initialized
        }

        // ---------------------------
        // 1. Create Categories
        // ---------------------------
        List<String> categoryNames = Arrays.asList(
                "Electronics", "Home Appliances", "Fashion", "Sports", "Books"
        );

        List<Category> categories = categoryNames.stream()
                .map(name -> Category.builder()
                        .name(name)
                        .description("Auto-generated category for " + name)
                        .build()
                ).toList();

        categoryRepository.saveAll(categories);

        // ---------------------------
        // 2. Create Random Products
        // ---------------------------
        for (int i = 0; i < 30; i++) {

            Category randomCategory = categories.get(random.nextInt(categories.size()));

            Product product = Product.builder()
                    .name(faker.commerce().productName())
                    .sku("SKU-" + faker.number().numberBetween(10000, 99999))
                    .price(faker.number().randomDouble(2, 100, 30000))
                    .stock(faker.number().numberBetween(0, 500))
                    .status(faker.options().option("ACTIVE", "INACTIVE"))
                    .category(randomCategory)
                    .build();

            productRepository.save(product);
        }

        log.info("✔ Random Categories & Products inserted successfully!");
    }
}
