package com.programmers.library.dto.reserve.response;

import com.programmers.library.domain.reserve.Reserve;

import java.util.UUID;

public record ReserveResponseDto(UUID reserveId, String studentId, String studentName, Long seatId, String reserveStatus) {
    public static ReserveResponseDto from(Reserve reserve) {
        return new ReserveResponseDto(
                reserve.getReserveId(),
                reserve.getStudentId().studentId(),
                reserve.getStudentName(),
                reserve.getSeat().getSeatId(),
                reserve.getReserveStatus().getDescription());
    }
}
