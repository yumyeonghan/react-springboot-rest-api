package com.programmers.library.domain.seat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeatTest {

    @DisplayName("테스트 종료 후 시퀀스 숫자를 초기화한다.")
    @AfterAll
    static void init() {
        SequenceNumberGenerator.initNumber();
    }

    @DisplayName("카테고리를 변경할 수 있다.")
    @Test
    void changeCategory() {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        LocalDateTime prevUpdatedAt = seat.getUpdatedAt();

        //when
        seat.changeCategory(Category.OPEN);

        //then
        assertThat(seat.getCategory()).isEqualTo (Category.OPEN);
        assertThat(seat.getUpdatedAt()).isAfter(prevUpdatedAt);
    }

    @DisplayName("좌석 상태를 수정할 수 있다.")
    @ParameterizedTest
    @EnumSource(SeatStatus.class)
    void changeSeatStatus(SeatStatus seatStatus) {
        //given
        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, seatStatus, LocalDateTime.now());
        LocalDateTime prevUpdatedAt = seat.getUpdatedAt();

        //when
        seat.changeSeatStatus();

        //then
        SeatStatus expectedStatus = (seatStatus == SeatStatus.RESERVATION_POSSIBLE)
                ? SeatStatus.RESERVATION_NOT_POSSIBLE
                : SeatStatus.RESERVATION_POSSIBLE;

        assertThat(seat.getSeatStatus()).isEqualTo(expectedStatus);
        assertThat(seat.getUpdatedAt()).isAfter(prevUpdatedAt);
    }

    @DisplayName("좌석 아이디가 비어있으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenSeatIdIsNull() {
        //given
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        //when, then
        assertThatThrownBy(() -> new Seat(createdAt, null, Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, updatedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("좌석 아이디가 비어있습니다.");
    }
}
