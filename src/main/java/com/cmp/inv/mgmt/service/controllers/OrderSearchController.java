package com.cmp.inv.mgmt.service.controllers;

import com.cmp.inv.mgmt.service.records.FieldMetadata;
import com.cmp.inv.mgmt.service.records.SearchKey;
import com.cmp.inv.mgmt.service.registry.SearchMetadataRegistry;
import com.cmp.inv.mgmt.service.request.QueryRequest;
import com.cmp.inv.mgmt.service.response.OrderResponse;
import com.cmp.inv.mgmt.service.services.OrderSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1")
public class OrderSearchController {

    private final OrderSearchService searchService;
    private final SearchMetadataRegistry registry;

    @Autowired
    public OrderSearchController(OrderSearchService searchService,
                                 SearchMetadataRegistry registry) {
        this.searchService = searchService;
        this.registry = registry;
    }

    @PostMapping("/orders/search")
    public Page<OrderResponse> search(@RequestBody QueryRequest request) {
        return searchService.search(request);
    }

    @GetMapping("/search/metadata")
    public Map<SearchKey, FieldMetadata> getSearchMetadata() {
        return registry.getMetadata();
    }

    @GetMapping("/search/key/mappings")
    public Map<String, SearchKey> getKeyMappings() {
        return registry.getKeyMapping();
    }
}
