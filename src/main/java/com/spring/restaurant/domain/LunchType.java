package com.spring.restaurant.domain;

import java.time.LocalTime;

/**
 * Enum of Lunch Types
 */

public enum LunchType {
    BREAKFAST(LocalTime.of(7,0), LocalTime.of(10, 0)),
    LUNCH(LocalTime.of(13,0), LocalTime.of(15, 0)),
    HOLIDAY(LocalTime.of(11,0), LocalTime.of(22, 0));

    private LocalTime timeFrom;
    private LocalTime timeTo;

    LunchType(LocalTime timeFrom, LocalTime timeTo) {
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
