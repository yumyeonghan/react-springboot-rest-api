package com.programmers.library.dto.reserve.request;

import java.util.UUID;

public record SeatReservationUpdateRequestDto(UUID reserveId, Long seatId){
}
