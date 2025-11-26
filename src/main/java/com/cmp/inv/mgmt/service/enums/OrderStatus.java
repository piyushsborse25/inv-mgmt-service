package com.cmp.inv.mgmt.service.enums;

import lombok.Getter;

import java.util.Random;

@Getter
public enum OrderStatus {

    PENDING("Order received"),
    PROCESSING("Getting ready"),
    SHIPPED("On the way"),
    DELIVERED("Arrived!"),
    CANCELLED("Stopped"),
    PLACED("Order confirmed");

    private final String message;

    OrderStatus(String message) {
        this.message = message;
    }

    private static final OrderStatus[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();

    public static OrderStatus randomStatus() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
