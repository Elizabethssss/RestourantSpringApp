package com.spring.restaurant.entity;

/**
 * Enum of Order Status Entities
 */

public enum OrderStatusEntity {
    FORMED("Making an order"),
    SENT("Waiting for confirmation"),
    CONFIRMED("Waiting for payment"),
    PAYED("Done");

    private String statusDesc;

    OrderStatusEntity(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
}
