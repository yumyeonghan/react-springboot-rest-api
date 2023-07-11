package com.programmers.library.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reserve {
    private final UUID orderId;
    private final StudentId studentId;
    private Seat seat;
    private ReserveStatus reserveStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Reserve(UUID orderId, StudentId studentId, Seat seat, ReserveStatus reserveStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.studentId = studentId;
        this.seat = seat;
        this.reserveStatus = reserveStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public Seat getSeat() {
        return seat;
    }

    public ReserveStatus getReserveStatus() {
        return reserveStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void changeSeat(Seat seat) {
        this.seat = seat;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeReserveStatus(ReserveStatus reserveStatus) {
        this.reserveStatus = reserveStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
