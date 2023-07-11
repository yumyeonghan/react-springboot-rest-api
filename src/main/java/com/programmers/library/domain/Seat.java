package com.programmers.library.domain;

import java.time.LocalDateTime;

public class Seat {
    private final LocalDateTime createdAt;

    private Long seatId;
    private Category category;
    private SeatStatus seatStatus;
    private LocalDateTime updatedAt;

    public Seat(LocalDateTime createdAt, Category category, SeatStatus seatStatus, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.category = category;
        this.seatStatus = seatStatus;
        this.updatedAt = updatedAt;
    }

    public Long getSeatId() {
        return seatId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Category getCategory() {
        return category;
    }

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void changeCategory(Category category) {
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }
}
