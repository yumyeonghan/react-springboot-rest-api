package com.programmers.library.domain.reserve;

import com.programmers.library.domain.seat.Category;
import com.programmers.library.domain.seat.Seat;
import com.programmers.library.domain.seat.SeatStatus;
import com.programmers.library.domain.seat.SequenceNumberGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReserveTest {

    @DisplayName("테스트 종료 후 시퀀스 숫자를 초기화한다.")
    @AfterAll
    static void init() {
        SequenceNumberGenerator.initNumber();
    }

    @DisplayName("예약 아이디가 비어있으면 예외가 발생한다.")
    @Test
    void throwExceptionReserveIdIsNull() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        StudentId studentId = new StudentId("201811612");
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        //when, then
        assertThatThrownBy(() -> new Reserve(null, studentId, "홍길동", seat, ReserveStatus.COMPLETED, createdAt, updatedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 아이디가 비어있습니다.");

    }

    @DisplayName("학생 이름이 비어있으면 예외가 발생한다.")
    @Test
    void throwExceptionStudentNameIsNull() {
        //given
        UUID reserveId = UUID.randomUUID();
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        StudentId studentId = new StudentId("201811612");
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        //when, then
        assertThatThrownBy(() -> new Reserve(reserveId, studentId, null, seat, ReserveStatus.COMPLETED, createdAt, updatedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("학생 이름이 비어있습니다.");
    }

    @DisplayName("좌석을 변경할 수 있다.")
    @Test
    void changeSeat() {
        //given
        UUID reserveId = UUID.randomUUID();
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        StudentId studentId = new StudentId("201811612");
        Reserve reserve = new Reserve(reserveId, studentId, "홍길동", seat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        LocalDateTime prevUpdatedAt = reserve.getUpdatedAt();

        //when
        Seat changeSeat = new Seat(LocalDateTime.now(), Category.OPEN, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        reserve.changeSeat(changeSeat);

        //then
        assertThat(reserve.getSeat()).isEqualTo(changeSeat);
        assertThat(reserve.getUpdatedAt()).isAfter(prevUpdatedAt);
    }

    @DisplayName("예약 상태를 변경할 수 있다.")
    @Test
    void changeReserveStatus() {
        //given
        UUID reserveId = UUID.randomUUID();
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        StudentId studentId = new StudentId("201811612");
        Reserve reserve = new Reserve(reserveId, studentId, "홍길동", seat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        LocalDateTime prevUpdatedAt = reserve.getUpdatedAt();

        //when
        reserve.changeReserveStatus(ReserveStatus.NOTHING);

        //then
        assertThat(reserve.getReserveStatus()).isEqualTo(ReserveStatus.NOTHING);
        assertThat(reserve.getUpdatedAt()).isAfter(prevUpdatedAt);
    }
}
