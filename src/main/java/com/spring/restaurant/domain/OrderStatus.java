package com.spring.restaurant.domain;

/**
 *  Enum of Order Statuses
 */

public enum OrderStatus {
    FORMED("Making an order", ""),
    SENT("Waiting for confirmation", "badge-info"),
    CONFIRMED("Waiting for payment", "badge-danger"),
    PAYED("Done", "badge-success");

    private String statusDesc;
    private String badge;

    OrderStatus(String statusDesc, String badge) {
        this.statusDesc = statusDesc;
        this.badge = badge;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public String getBadge() {
        return badge;
    }
}
