package com.programmers.library.service.seat;

import com.programmers.library.domain.seat.Category;
import com.programmers.library.domain.seat.Seat;
import com.programmers.library.domain.seat.SeatStatus;
import com.programmers.library.dto.seat.request.SeatCreateRequestDto;
import com.programmers.library.dto.seat.request.SeatUpdateRequestDto;
import com.programmers.library.dto.seat.response.PageSeatResponseDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;
import com.programmers.library.repository.seat.JdbcSeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final JdbcSeatRepository jdbcSeatRepository;

    @Override
    public SeatResponseDto createSeat(SeatCreateRequestDto seatCreateRequestDto) {
        Category category = Category.find(seatCreateRequestDto.category());
        Seat seat = new Seat(LocalDateTime.now(), category, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);
        return SeatResponseDto.from(seat);
    }

    @Override
    public void deleteSeat(Long seatId) {
        jdbcSeatRepository.deleteById(seatId);
    }

    @Override
    public SeatResponseDto updateSeatStatus(Long seatId) {
        Seat seat = jdbcSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s번 좌석이 존재하지 않습니다.", seatId)));
        seat.changeSeatStatus();
        jdbcSeatRepository.update(seat);
        return SeatResponseDto.from(seat);
    }

    @Override
    public SeatResponseDto updateSeatCategory(SeatUpdateRequestDto seatUpdateRequestDto) {
        Long seatId = seatUpdateRequestDto.seatId();
        Category category = Category.find(seatUpdateRequestDto.category());
        Seat seat = jdbcSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s번 좌석이 존재하지 않습니다.", seatId)));
        seat.changeCategory(category);
        jdbcSeatRepository.update(seat);
        return SeatResponseDto.from(seat);
    }

    @Override
    public List<SeatResponseDto> findSeatList() {
        return jdbcSeatRepository.findAll()
                .stream()
                .map(SeatResponseDto::from)
                .toList();
    }

    @Override
    public List<SeatResponseDto> findSeatListByCategory(String category) {
        return jdbcSeatRepository.findAllByCategory(Category.find(category))
                .stream()
                .map(SeatResponseDto::from)
                .toList();
    }

    @Override
    public SeatResponseDto findSeat(Long seatId) {
        return jdbcSeatRepository.findById(seatId)
                .map(SeatResponseDto::from)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s번 좌석이 존재하지 않습니다.", seatId)));
    }

    @Override
    public PageSeatResponseDto findSeatListByPage(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        int totalSeatCount = jdbcSeatRepository.getCount();
        int totalPages = (int) Math.ceil((double) totalSeatCount / pageSize);
        List<SeatResponseDto> seats = jdbcSeatRepository.findAllByPage(offset, pageSize).stream().map(SeatResponseDto::from).toList();
        return PageSeatResponseDto.from(seats, totalPages);
    }
}
