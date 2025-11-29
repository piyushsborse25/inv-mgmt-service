package com.cmp.inv.mgmt.service.entities;

import com.cmp.inv.mgmt.service.annotations.SearchRoot;
import com.cmp.inv.mgmt.service.annotations.Searchable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SearchRoot(path = "Order.customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Searchable
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    @Searchable
    private String email;

    @NotBlank
    @Size(min = 6)
    @ToString.Exclude
    private String password;

    @NotBlank
    @Column(name = "full_name")
    @Searchable
    private String fullName;

    @Column(name = "billing_address")
    @ToString.Exclude
    private String billingAddress;

    @Column(name = "default_shipping_address")
    @ToString.Exclude
    private String defaultShippingAddress;

    @Searchable
    private String country;

    @Searchable
    private String phone;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    @ToString.Exclude
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        if (!orders.contains(order)) {
            orders.add(order);
            order.setCustomer(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(getEmail(), customer.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEmail());
    }
}
