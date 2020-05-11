package com.spring.restaurant.entity;

import java.time.LocalTime;

/**
 * Enum of Lunch Type Entities
 */

public enum LunchTypeEntity {
    BREAKFAST(LocalTime.of(7,0), LocalTime.of(10, 0)),
    LUNCH(LocalTime.of(13,0), LocalTime.of(15, 0)),
    HOLIDAY(LocalTime.of(11,0), LocalTime.of(22, 0));

    private LocalTime timeFrom;
    private LocalTime timeTo;

    LunchTypeEntity(LocalTime timeFrom, LocalTime timeTo) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }
}
