package com.programmers.library.service.reserve;

import com.programmers.library.domain.reserve.Reserve;
import com.programmers.library.domain.reserve.ReserveStatus;
import com.programmers.library.domain.reserve.StudentId;
import com.programmers.library.domain.seat.Seat;
import com.programmers.library.dto.reserve.request.ReserveCreateRequestDto;
import com.programmers.library.dto.reserve.request.SeatReservationUpdateRequestDto;
import com.programmers.library.dto.reserve.response.ReserveResponseDto;
import com.programmers.library.repository.reserve.JdbcReserveRepository;
import com.programmers.library.repository.seat.JdbcSeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReserveServiceImpl implements ReserveService{

    private final JdbcReserveRepository jdbcReserveRepository;
    private final JdbcSeatRepository jdbcSeatRepository;

    @Override
    public ReserveResponseDto createReserve(ReserveCreateRequestDto reserveCreateRequestDto) {
        String studentId = reserveCreateRequestDto.studentId();
        String studentName = reserveCreateRequestDto.studentName();
        Long seatId = reserveCreateRequestDto.seatId();
        Seat seat = jdbcSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s번 좌석이 존재하지 않습니다.", seatId)));
        Reserve reserve = new Reserve(UUID.randomUUID(), new StudentId(studentId), studentName, seat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        jdbcReserveRepository.insert(reserve);
        return ReserveResponseDto.from(reserve);
    }

    @Override
    public void deleteReserve(UUID reserveId) {
        jdbcReserveRepository.deleteById(reserveId);
    }

    @Override
    public ReserveResponseDto updateFailedReserveStatus(UUID reserveId) {
        Reserve reserve = jdbcReserveRepository.findById(reserveId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s 예약이 존재하지 않습니다.", reserveId)));
        reserve.changeReserveStatus(ReserveStatus.FAILED);
        jdbcReserveRepository.update(reserve);
        return ReserveResponseDto.from(reserve);
    }

    @Override
    public ReserveResponseDto updateSeat(SeatReservationUpdateRequestDto seatReservationUpdateRequestDto) {
        Long seatId = seatReservationUpdateRequestDto.seatId();
        UUID reserveId = seatReservationUpdateRequestDto.reserveId();
        Seat seat = jdbcSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s번 좌석이 존재하지 않습니다.", seatId)));
        Reserve reserve = jdbcReserveRepository.findById(reserveId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s 예약이 존재하지 않습니다.", reserveId)));
        reserve.changeSeat(seat);
        jdbcReserveRepository.update(reserve);
        return ReserveResponseDto.from(reserve);
    }

    @Override
    public List<ReserveResponseDto> findReserveList() {
        return jdbcReserveRepository.findAll()
                .stream()
                .map(ReserveResponseDto::from)
                .toList();
    }

    @Override
    public ReserveResponseDto findReserve(UUID reserveId) {
        Reserve reserve = jdbcReserveRepository.findById(reserveId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s 예약이 존재하지 않습니다.", reserveId)));
        return ReserveResponseDto.from(reserve);
    }
}
