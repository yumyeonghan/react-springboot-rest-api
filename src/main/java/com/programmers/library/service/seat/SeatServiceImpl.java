package com.programmers.library.service.seat;

import com.programmers.library.domain.seat.Category;
import com.programmers.library.domain.seat.Seat;
import com.programmers.library.domain.seat.SeatStatus;
import com.programmers.library.dto.seat.SeatCreateRequestDto;
import com.programmers.library.dto.seat.SeatResponseDto;
import com.programmers.library.dto.seat.SeatUpdateRequestDto;
import com.programmers.library.repository.seat.JdbcSeatRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class SeatServiceImpl implements SeatService{

    private final JdbcSeatRepository jdbcSeatRepository;

    @Override
    public Long createSeat(SeatCreateRequestDto seatCreateRequestDto) {
        Category category = Category.find(seatCreateRequestDto.category());
        Seat seat = new Seat(LocalDateTime.now(), category, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);
        return seat.getSeatId();
    }

    @Override
    public void deleteSeat(Long seatId) {
        jdbcSeatRepository.deleteById(seatId);
    }

    @Override
    public Long updateSeatStatus(Long seatId) {
        Seat seat = jdbcSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s번 좌석이 존재하지 않습니다.", seatId)));
        seat.changeSeatStatus();
        jdbcSeatRepository.update(seat);
        return seat.getSeatId();
    }

    @Override
    public Long updateSeatCategory(SeatUpdateRequestDto seatUpdateRequestDto) {
        Long seatId = seatUpdateRequestDto.seatId();
        Category category = Category.find(seatUpdateRequestDto.category());
        Seat seat = jdbcSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s번 좌석이 존재하지 않습니다.", seatId)));
        seat.changeCategory(category);
        jdbcSeatRepository.update(seat);
        return seat.getSeatId();
    }

    @Override
    public List<SeatResponseDto> findSeatList() {
        return jdbcSeatRepository.findAll()
                .stream()
                .map(SeatResponseDto::from)
                .toList();
    }

    @Override
    public SeatResponseDto findSeatBySeatId(Long seatId) {
        return jdbcSeatRepository.findById(seatId)
                .map(SeatResponseDto::from)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s번 좌석이 존재하지 않습니다.", seatId)));
    }
}
