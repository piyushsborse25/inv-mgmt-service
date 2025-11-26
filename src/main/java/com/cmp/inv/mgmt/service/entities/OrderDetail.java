package com.cmp.inv.mgmt.service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    private Integer quantity;

    public void setOrder(Order order) {
        this.order = order;
        if (order != null && !order.getOrderDetails().contains(this)) {
            order.getOrderDetails().add(this);
        }
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null && !product.getOrderDetails().contains(this)) {
            product.getOrderDetails().add(this);
        }
    }
}