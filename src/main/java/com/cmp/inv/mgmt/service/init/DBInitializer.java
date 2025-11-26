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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DBInitializer {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    private final Faker faker = new Faker();

    @PostConstruct
    public void init() {

        if (categoryRepository.count() > 0 ||
                productRepository.count() > 0 ||
                customerRepository.count() > 0 ||
                orderRepository.count() > 0)
            return;

        // 1️⃣ Seed Categories
        List<Category> categories = IntStream.range(0, 5)
                .mapToObj(i -> Category.builder()
                        .name(faker.commerce().department())
                        .description(faker.lorem().sentence())
                        .build())
                .collect(Collectors.toList());
        categoryRepository.saveAll(categories);

        // 2️⃣ Seed Products and attach to Category using helper method
        List<Product> products = IntStream.range(0, 20)
                .mapToObj(i -> {
                    Category category = categories.get(faker.random().nextInt(categories.size()));
                    Product product = Product.builder()
                            .sku("SKU-" + faker.number().digits(8))
                            .name(faker.commerce().productName())
                            .description(faker.lorem().sentence())
                            .price(faker.number().randomDouble(2, 50, 2000))
                            .stock(faker.number().numberBetween(10, 200))
                            .imageUrl(faker.internet().image())
                            .build();
                    category.addProduct(product);
                    return product;
                })
                .collect(Collectors.toList());
        productRepository.saveAll(products);

        // 3️⃣ Seed Customers
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
                .collect(Collectors.toList());
        customerRepository.saveAll(customers);

        // 4️⃣ Create Orders + OrderDetails using helper methods
        for (int i = 0; i < 40; i++) {
            Customer customer = customers.get(faker.random().nextInt(customers.size()));
            Order order = Order.builder()
                    .status(OrderStatus.PLACED)
                    .totalPrice(0.0)
                    .build();

            customer.addOrder(order);

            double total = 0.0;
            int items = faker.random().nextInt(1, 4);

            for (int j = 0; j < items; j++) {
                Product product = products.get(faker.random().nextInt(products.size()));
                int qty = faker.number().numberBetween(1, 5);

                OrderDetail detail = OrderDetail.builder()
                        .quantity(qty)
                        .build();

                order.addOrderDetail(detail);   // maintain relationship
                product.addOrderDetail(detail); // maintain other side

                total += product.getPrice() * qty;
            }

            order.setTotalPrice(total);
            orderRepository.save(order); // cascades save for details
        }

        System.out.println("🎯 Data seeded successfully using bidirectional helper methods");
    }
}
