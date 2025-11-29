package com.cmp.inv.mgmt.service.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long id;
    private String orderStatus;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private String customerName;
    private String customerEmail;
}
