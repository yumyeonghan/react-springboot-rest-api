package com.programmers.library.domain.reserve;

public enum ReserveStatus {
    FAILED("예약실패"),
    COMPLETED("예약성공"),
    ;

    private final String description;

    ReserveStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
