package com.programmers.library.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Seat {
    private final UUID seatId;
    private final LocalDateTime createdAt;

    private String seatNumber;
    private Category category;
    private SeatStatus seatStatus;
    private LocalDateTime updatedAt;

    public Seat(UUID seatId, Category category, SeatStatus seatStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.seatId = seatId;
        this.createdAt = createdAt;
        this.category = category;
        this.seatStatus = seatStatus;
        this.updatedAt = updatedAt;
    }

    public Seat(UUID seatId, String seatNumber, Category category, SeatStatus seatStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.seatId = seatId;
        this.createdAt = createdAt;
        this.seatNumber = seatNumber;
        this.category = category;
        this.seatStatus = seatStatus;
        this.updatedAt = updatedAt;
    }

    public UUID getSeatId() {
        return seatId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getSeatNumber() {
        return seatNumber;
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
}
