package com.cmp.inv.mgmt.service.init;

import com.cmp.inv.mgmt.service.entities.*;
import com.cmp.inv.mgmt.service.enums.OrderStatus;
import com.cmp.inv.mgmt.service.repos.CategoryRepository;
import com.cmp.inv.mgmt.service.repos.CustomerRepository;
import com.cmp.inv.mgmt.service.repos.OrderRepository;
import com.cmp.inv.mgmt.service.repos.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DBInitializer {

    private static final Logger log = LogManager.getLogger(DBInitializer.class);
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    private final Faker faker = new Faker();

    @PostConstruct
    public void init() {

        if (categoryRepository.count() > 0
                || productRepository.count() > 0
                || customerRepository.count() > 0
                || orderRepository.count() > 0)
            return;

        // Seed Categories
        List<Category> categories = IntStream.range(0, 5).
                mapToObj(i -> Category.builder()
                        .name(faker.commerce().department())
                        .description(faker.lorem().sentence())
                        .build())
                .toList();
        categories = categoryRepository.saveAll(categories);
        log.info("Saved categories");

        // Seed Products and attach to Category using helper method
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Category category = categories.get(faker.random().nextInt(categories.size()));
            Product product = Product.builder().
                    sku("SKU-" + faker.number().digits(8))
                    .name(faker.commerce().productName())
                    .description(faker.lorem().sentence())
                    .price(BigDecimal.valueOf(faker.number().randomDouble(2, 50, 2000)))
                    .stock(faker.number().numberBetween(10, 200))
                    .imageUrl(faker.internet().image())
                    .build();
            product.setCategory(category);
            products.add(product);
        }
        products = productRepository.saveAll(products);
        log.info("Saved products");

        // Seed Customers
        List<Customer> customers = IntStream.range(0, 10)
                .mapToObj(i -> Customer.builder()
                        .email(faker.internet().emailAddress())
                        .password("pass" + faker.number().digits(4))
                        .fullName(faker.name().fullName())
                        .billingAddress(faker.address().fullAddress())
                        .defaultShippingAddress(faker.address().streetAddress())
                        .country(faker.address().country())
                        .phone(faker.phoneNumber().cellPhone())
                        .build())
                .toList();
        customers = customerRepository.saveAll(customers);
        log.info("Saved customers");

        // 4️⃣ Create Orders + OrderDetails using helper methods
        for (int i = 0; i < 40; i++) {
            Customer customer = customers.get(faker.random().nextInt(customers.size()));

            // Create Order
            Order order = Order.builder().status(OrderStatus.randomStatus()).totalPrice(BigDecimal.ZERO).build();
            order.setCustomer(customer);

            double total = 0.0;
            int items = faker.random().nextInt(1, 4);

            for (int j = 0; j < items; j++) {
                Product product = products.get(faker.random().nextInt(products.size()));
                int qty = faker.number().numberBetween(1, 5);
                OrderDetail detail = OrderDetail.builder().quantity(qty).build();
                detail.setOrder(order);
                detail.setProduct(product);
                total += product.getPrice().doubleValue() * qty;
            }

            order.setTotalPrice(BigDecimal.valueOf(total));
            log.info("Before Saved order");
            orderRepository.save(order);
            log.info("After Saved order");
        }

        log.info("Data seeded successfully using bidirectional helper methods");
    }
}
