package com.cmp.inv.mgmt.service.entities;

import com.cmp.inv.mgmt.service.annotations.SearchRoot;
import com.cmp.inv.mgmt.service.annotations.Searchable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SearchRoot(path = "Order.orderDetails.product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Searchable
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Searchable
    private String sku;

    @NotBlank
    @Size(max = 255)
    @Searchable
    private String name;

    @Column(length = 500)
    @Searchable
    private String description;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    @Searchable
    private BigDecimal price;

    @NotNull
    @Column(nullable = false)
    @Searchable
    private Integer stock;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void setCategory(Category category) {
        this.category = category;
        if (category != null && !category.getProducts().contains(this)) {
            category.getProducts().add(this);
        }
    }

    public void addOrderDetail(OrderDetail detail) {
        if (!orderDetails.contains(detail)) {
            orderDetails.add(detail);
            detail.setProduct(this);
        }
    }
}
