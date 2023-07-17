package com.programmers.library.dto.seat.response;

import com.programmers.library.domain.seat.Seat;

public record SeatResponseDto(Long seatId, String category, String seatStatus) {
    public static SeatResponseDto from(Seat seat) {
        return new SeatResponseDto(seat.getSeatId(), seat.getCategory().getDescription(), seat.getSeatStatus().getDescription());
    }
}
