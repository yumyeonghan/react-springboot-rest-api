package com.programmers.library.service.reserve;

import com.programmers.library.dto.reserve.request.ReserveCreateRequestDto;
import com.programmers.library.dto.reserve.request.SeatReservationUpdateRequestDto;
import com.programmers.library.dto.reserve.response.ReserveResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReserveService {
    ReserveResponseDto createReserve(ReserveCreateRequestDto reserveCreateRequestDto);

    void deleteReserve(UUID reserveId);

    ReserveResponseDto updateFailedReserveStatus(UUID reserveId);

    ReserveResponseDto updateSeat(SeatReservationUpdateRequestDto seatReservationUpdateRequestDto);

    List<ReserveResponseDto> findReserveList();

    ReserveResponseDto findReserve(UUID reserveId);
}
