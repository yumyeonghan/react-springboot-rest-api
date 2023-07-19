package com.programmers.library.repository.reserve;

import com.programmers.library.domain.reserve.Reserve;
import com.programmers.library.domain.reserve.ReserveStatus;
import com.programmers.library.domain.reserve.StudentId;
import com.programmers.library.domain.seat.Category;
import com.programmers.library.domain.seat.Seat;
import com.programmers.library.domain.seat.SeatStatus;
import com.programmers.library.repository.seat.JdbcSeatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class JdbcReserveRepositoryTest {

    @Autowired
    private JdbcReserveRepository jdbcReserveRepository;

    @Autowired
    private JdbcSeatRepository jdbcSeatRepository;

    @DisplayName("전체 예약 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        //given
        final int expectedSize = 2;

        Seat seat1 = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat1);
        Reserve reserve1 = new Reserve(UUID.randomUUID(), new StudentId("201811612"), "홍길동", seat1, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        jdbcReserveRepository.insert(reserve1);

        Seat seat2 = new Seat(LocalDateTime.now(), Category.OPEN, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat2);
        Reserve reserve2 = new Reserve(UUID.randomUUID(), new StudentId("201811111"), "김길동", seat2, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        jdbcReserveRepository.insert(reserve2);

        //when
        List<Reserve> reserveList = jdbcReserveRepository.findAll();

        //then
        assertThat(reserveList).hasSize(expectedSize);
    }

    @DisplayName("예약을 할 수 있다.")
    @Test
    void insert() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);
        Reserve reserve = new Reserve(UUID.randomUUID(), new StudentId("201811612"), "홍길동", seat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());

        //when
        Reserve insertedReserve = jdbcReserveRepository.insert(reserve);

        //then
        assertThat(insertedReserve).isEqualTo(reserve);

    }

    @DisplayName("예약을 수정할 수 있다.")
    @Test
    void update() {
        //given
        Seat prevSeat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(prevSeat);

        Seat nextSeat = new Seat(LocalDateTime.now(), Category.OPEN, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(nextSeat);

        Reserve reserve = new Reserve(UUID.randomUUID(), new StudentId("201811612"), "홍길동", prevSeat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        jdbcReserveRepository.insert(reserve);
        LocalDateTime prevUpdatedAt = reserve.getUpdatedAt();
        
        //when
        reserve.changeReserveStatus(ReserveStatus.FAILED);
        reserve.changeSeat(nextSeat);
        jdbcReserveRepository.update(reserve);

        //then
        assertThat(reserve.getUpdatedAt()).isAfter(prevUpdatedAt);
        assertThat(reserve.getSeat()).isEqualTo(nextSeat);
        assertThat(reserve.getReserveStatus()).isEqualTo(ReserveStatus.FAILED);
    }

    @DisplayName("식별자로 예약을 조회할 수 있다.")
    @Test
    void findById() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);

        UUID reserveId = UUID.randomUUID();
        Reserve reserve = new Reserve(reserveId, new StudentId("201811612"), "홍길동", seat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        jdbcReserveRepository.insert(reserve);

        //when
        Reserve foundReserve = jdbcReserveRepository.findById(reserveId).get();

        //then
        assertThat(foundReserve).usingRecursiveComparison().isEqualTo(reserve);
    }

    @DisplayName("학번으로 예약을 조회할 수 있다.")
    @Test
    void findByStudentId() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);

        UUID reserveId = UUID.randomUUID();
        StudentId studentId = new StudentId("201811612");
        Reserve reserve = new Reserve(reserveId, studentId, "홍길동", seat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        jdbcReserveRepository.insert(reserve);

        //when
        Reserve foundReserve = jdbcReserveRepository.findByStudentId(studentId.studentId()).get();

        //then
        assertThat(foundReserve).usingRecursiveComparison().isEqualTo(reserve);
    }

    @DisplayName("좌석번호로 예약을 조회할 수 있다.")
    @Test
    void findBySeatId() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);

        UUID reserveId = UUID.randomUUID();
        StudentId studentId = new StudentId("201811612");
        Reserve reserve = new Reserve(reserveId, studentId, "홍길동", seat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        jdbcReserveRepository.insert(reserve);

        //when
        Reserve foundReserve = jdbcReserveRepository.findBySeatId(seat.getSeatId()).get();

        //then
        assertThat(foundReserve).usingRecursiveComparison().isEqualTo(reserve);
    }

    @DisplayName("식별자로 예약을 삭제할 수 있다.")
    @Test
    void deleteById() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat);

        UUID reserveId = UUID.randomUUID();
        Reserve reserve = new Reserve(reserveId, new StudentId("201811612"), "홍길동", seat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        jdbcReserveRepository.insert(reserve);

        //when
        jdbcReserveRepository.deleteById(reserveId);

        //then
        Optional<Reserve> foundReserve = jdbcReserveRepository.findById(reserveId);
        assertThat(foundReserve).isEmpty();
    }
}
