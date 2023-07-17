package com.programmers.library.service.seat;

import com.programmers.library.dto.seat.request.SeatCreateRequestDto;
import com.programmers.library.dto.seat.request.SeatUpdateRequestDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;

import java.util.List;

public interface SeatService {
    Long createSeat(SeatCreateRequestDto seatCreateRequestDto);

    void deleteSeat(Long seatId);

    Long updateSeatStatus(Long seatId);

    Long updateSeatCategory(SeatUpdateRequestDto seatUpdateRequestDto);

    List<SeatResponseDto> findSeatList();

    SeatResponseDto findSeatBySeatId(Long seatId);
}
