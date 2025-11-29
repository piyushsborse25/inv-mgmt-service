package com.cmp.inv.mgmt.service.services;

import com.cmp.inv.mgmt.service.engine.DynamicSpecificationBuilder;
import com.cmp.inv.mgmt.service.entities.Order;
import com.cmp.inv.mgmt.service.enums.SortDirection;
import com.cmp.inv.mgmt.service.repos.OrderRepository;
import com.cmp.inv.mgmt.service.request.QueryRequest;
import com.cmp.inv.mgmt.service.request.SortRule;
import com.cmp.inv.mgmt.service.response.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class OrderSearchService {

    private final OrderRepository orderRepository;
    private final DynamicSpecificationBuilder specBuilder;

    @Autowired
    public OrderSearchService(OrderRepository orderRepository,
                              DynamicSpecificationBuilder specBuilder) {
        this.orderRepository = orderRepository;
        this.specBuilder = specBuilder;
    }

    public Page<OrderResponse> search(QueryRequest request) {
        Specification<Order> spec = specBuilder.build(request.getFilters());
        Pageable pageable = buildPageable(request.getSort(),
                                          request.getPage(),
                                          request.getSize());
        Page<Order> pageResult = orderRepository.findAll(spec, pageable);

        return new PageImpl<>(
                pageResult.getContent()
                        .stream()
                        .map(Order::toResponse)
                        .toList(),
                pageable,
                pageResult.getTotalElements()
        );
    }

    private Pageable buildPageable(SortRule sort, Integer page, Integer size) {

        int pageIndex = page != null ? page : 0;
        int pageSize = size != null ? size : 10;
        if (sort == null || sort.getField() == null) {
            return PageRequest.of(pageIndex, pageSize);
        }
        SortDirection dir = sort.getDirection() != null
                ? sort.getDirection()
                : SortDirection.ASC;
        Sort springSort = Sort.by(
                dir == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                sort.getField()
        );

        return PageRequest.of(pageIndex, pageSize, springSort);
    }
}
