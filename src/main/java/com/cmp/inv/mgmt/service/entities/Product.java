package com.cmp.inv.mgmt.service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 150, message = "Product name must be 2–150 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true)
    private String sku;

    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Price must be at least 1")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock must be >= 0")
    @Column(nullable = false)
    private Integer stock;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be ACTIVE or INACTIVE")
    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category is required")
    private Category category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void preInsert() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) status = "ACTIVE";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}