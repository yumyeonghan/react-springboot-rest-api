package com.programmers.library.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reserve {
    private final UUID reserveId;
    private final StudentId studentId;
    private final String studentName;
    private final LocalDateTime createdAt;

    private Seat seat;
    private ReserveStatus reserveStatus;
    private LocalDateTime updatedAt;

    public Reserve(UUID reserveId, StudentId studentId, String studentName, Seat seat, ReserveStatus reserveStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.reserveId = reserveId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.seat = seat;
        this.reserveStatus = reserveStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getReserveId() {
        return reserveId;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
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
