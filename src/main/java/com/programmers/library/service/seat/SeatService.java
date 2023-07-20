package com.programmers.library.service.seat;

import com.programmers.library.dto.seat.request.SeatCreateRequestDto;
import com.programmers.library.dto.seat.request.SeatUpdateRequestDto;
import com.programmers.library.dto.seat.response.PageSeatResponseDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;

import java.util.List;

public interface SeatService {
    SeatResponseDto createSeat(SeatCreateRequestDto seatCreateRequestDto);

    void deleteSeat(Long seatId);

    SeatResponseDto updateSeatStatus(Long seatId);

    SeatResponseDto updateSeatCategory(SeatUpdateRequestDto seatUpdateRequestDto);

    List<SeatResponseDto> findSeatList();

    List<SeatResponseDto> findSeatListByCategory(String category);

    SeatResponseDto findSeat(Long seatId);

    PageSeatResponseDto findSeatListByPage(int pageNumber, int pageSize);
}
