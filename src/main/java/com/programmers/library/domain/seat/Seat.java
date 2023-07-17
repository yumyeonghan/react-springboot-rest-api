package com.programmers.library.domain.seat;

import java.time.LocalDateTime;
import java.util.Objects;

public class Seat {
    private final LocalDateTime createdAt;

    private Long seatId;
    private Category category;
    private SeatStatus seatStatus;
    private LocalDateTime updatedAt;

    public Seat(LocalDateTime createdAt, Category category, SeatStatus seatStatus, LocalDateTime updatedAt) {
        this.seatId = SequenceNumberGenerator.getNumber();
        this.createdAt = createdAt;
        this.category = category;
        this.seatStatus = seatStatus;
        this.updatedAt = updatedAt;
    }

    public Seat(LocalDateTime createdAt, Long seatId, Category category, SeatStatus seatStatus, LocalDateTime updatedAt) {
        validateSeatId(seatId);
        this.createdAt = createdAt;
        this.seatId = seatId;
        this.category = category;
        this.seatStatus = seatStatus;
        this.updatedAt = updatedAt;
    }

    private static void validateSeatId(Long seatId) {
        if (Objects.isNull(seatId)) {
            throw new IllegalArgumentException("좌석 아이디가 비어있습니다.");
        }
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

    public void changeSeatStatus() {
        switch (this.seatStatus) {
            case RESERVATION_POSSIBLE -> this.seatStatus = SeatStatus.RESERVATION_NOT_POSSIBLE;
            case RESERVATION_NOT_POSSIBLE -> this.seatStatus = SeatStatus.RESERVATION_POSSIBLE;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }
}
