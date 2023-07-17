package com.programmers.library.repository.seat;

import com.programmers.library.domain.seat.Category;
import com.programmers.library.domain.seat.Seat;
import com.programmers.library.domain.seat.SeatStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class JdbcSeatRepositoryTest {

    @Autowired
    private JdbcSeatRepository jdbcSeatRepository;

    @DisplayName("전체 좌석 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        //given
        final int expectedSize = 2;

        Seat seat1 = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat1);

        Seat seat2 = new Seat(LocalDateTime.now(), Category.OPEN, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat2);

        //when
        List<Seat> seatList = jdbcSeatRepository.findAll();

        //then
        assertThat(seatList).hasSize(expectedSize);
    }

    @DisplayName("좌석을 생성할 수 있다.")
    @ParameterizedTest
    @EnumSource(Category.class)
    void insert(Category category) {
        //given
        Seat seat = new Seat(LocalDateTime.now(), category, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());

        //when
        Seat insertedSeat = jdbcSeatRepository.insert(seat);

        //then
        assertThat(insertedSeat).isEqualTo(seat);
    }

    @DisplayName("좌석을 수정할 수 있다.")
    @Test
    void update() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);
        LocalDateTime prevUpdatedAt = seat.getUpdatedAt();

        //when
        seat.changeCategory(Category.OPEN);
        seat.changeSeatStatus();
        Seat updatedSeat = jdbcSeatRepository.update(seat);

        //then
        assertThat(updatedSeat.getUpdatedAt()).isAfter(prevUpdatedAt);
        assertThat(updatedSeat.getCategory()).isEqualTo(seat.getCategory());
        assertThat(updatedSeat.getSeatStatus()).isEqualTo(seat.getSeatStatus());
    }

    @DisplayName("식별자로 좌석을 조회할 수 있다.")
    @Test
    void findById() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);

        //when
        Seat foundSeat = jdbcSeatRepository.findById(seat.getSeatId()).get();

        //then
        assertThat(foundSeat).usingRecursiveComparison().isEqualTo(seat);
    }

    @DisplayName("식별자로 좌석을 삭제할 수 있다.")
    @Test
    void deletedById() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);

        //when
        jdbcSeatRepository.deleteById(seat.getSeatId());

        //then
        Optional<Seat> foundSeat = jdbcSeatRepository.findById(seat.getSeatId());
        assertThat(foundSeat).isEmpty();
    }

    @DisplayName("카테고리별로 좌석을 조회할 수 있다.")
    @ParameterizedTest
    @EnumSource(Category.class)
    void findAllByCategory(Category category) {
        //given
        int expectedSize = 1;

        Seat closedSeat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(closedSeat);

        Seat openSeat = new Seat(LocalDateTime.now(), Category.OPEN, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(openSeat);

        //when
        List<Seat> seatList = jdbcSeatRepository.findAllByCategory(category);

        //then
        assertThat(seatList).hasSize(expectedSize);
        assertThat(seatList.stream().allMatch(seat -> seat.getCategory() == category)).isTrue();
    }
}
