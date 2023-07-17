package com.programmers.library.service.seat;

import com.programmers.library.dto.seat.SeatCreateRequestDto;
import com.programmers.library.dto.seat.SeatResponseDto;
import com.programmers.library.dto.seat.SeatUpdateRequestDto;

import java.util.List;

public interface SeatService {
    Long createSeat(SeatCreateRequestDto seatCreateRequestDto);

    void deleteSeat(Long seatId);

    Long updateSeatStatus(Long seatId);

    Long updateSeatCategory(SeatUpdateRequestDto seatUpdateRequestDto);

    List<SeatResponseDto> findSeatList();

    SeatResponseDto findSeatBySeatId(Long seatId);
}
