package com.programmers.library.dto.seat.response;

import java.util.List;

public record PageSeatResponseDto(List<SeatResponseDto> seats, int totalPages) {
    public static PageSeatResponseDto from(List<SeatResponseDto> seats, int totalPages) {
        return new PageSeatResponseDto(seats, totalPages);
    }
}
