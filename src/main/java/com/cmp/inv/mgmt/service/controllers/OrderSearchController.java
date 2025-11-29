package com.cmp.inv.mgmt.service.controllers;

import com.cmp.inv.mgmt.service.entities.Order;
import com.cmp.inv.mgmt.service.request.QueryRequest;
import com.cmp.inv.mgmt.service.response.OrderResponse;
import com.cmp.inv.mgmt.service.services.OrderSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class OrderSearchController {

    private final OrderSearchService searchService;

    @Autowired
    public OrderSearchController(OrderSearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/orders/search")
    public Page<OrderResponse> search(@RequestBody QueryRequest request) {
        return searchService.search(request);
    }
}
