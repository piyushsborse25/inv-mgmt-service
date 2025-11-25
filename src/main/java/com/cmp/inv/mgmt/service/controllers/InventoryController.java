package com.cmp.inv.mgmt.service.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class InventoryController {

    @PostMapping("/products/search")
    public Object searchProducts() {
        return null;
    }

    @PostMapping("/orders/search")
    public Object searchOrders() {
        return null;
    }

    @PostMapping("/customers/search")
    public Object searchCustomers() {
        return null;
    }

    @PostMapping("/categories/search")
    public Object searchCategories() {
        return null;
    }
}

