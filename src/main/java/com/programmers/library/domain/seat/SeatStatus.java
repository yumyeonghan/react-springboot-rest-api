package com.programmers.library.domain.seat;

public enum SeatStatus {
    RESERVATION_POSSIBLE("예약가능"),
    RESERVATION_NOT_POSSIBLE("예약불가")
    ;

    private final String description;

    SeatStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
