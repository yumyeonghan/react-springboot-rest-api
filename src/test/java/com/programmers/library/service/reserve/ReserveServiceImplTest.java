package com.programmers.library.service.reserve;

import com.programmers.library.domain.reserve.Reserve;
import com.programmers.library.domain.reserve.ReserveStatus;
import com.programmers.library.domain.seat.Category;
import com.programmers.library.dto.reserve.request.ReserveCreateRequestDto;
import com.programmers.library.dto.reserve.request.SeatReservationUpdateRequestDto;
import com.programmers.library.dto.reserve.response.ReserveResponseDto;
import com.programmers.library.dto.seat.request.SeatCreateRequestDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;
import com.programmers.library.repository.reserve.JdbcReserveRepository;
import com.programmers.library.service.seat.SeatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReserveServiceImplTest {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private JdbcReserveRepository jdbcReserveRepository;

    @DisplayName("예약을 생성할 수 있다.")
    @Test
    void createReserve() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);

        ReserveCreateRequestDto reserveCreateRequestDto = new ReserveCreateRequestDto("201811612", "홍길동", seat.seatId());

        //when
        ReserveResponseDto reserve = reserveService.createReserve(reserveCreateRequestDto);

        //then
        ReserveResponseDto foundReserve = reserveService.findReserve(reserve.reserveId());
        assertThat(foundReserve).isEqualTo(reserve);
    }

    @DisplayName("예약할 좌석이 존재하지 않으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenSeatToReserveIsNull() {
        //given
        Long seatId = new Random().nextLong();
        ReserveCreateRequestDto reserveCreateRequestDto = new ReserveCreateRequestDto("201811612", "홍길동", seatId);

        //then, then
        assertThatThrownBy(() -> reserveService.createReserve(reserveCreateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s번 좌석이 존재하지 않습니다.", seatId));
    }

    @DisplayName("학생이 여러번 예약하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenStudentReservesMultipleTimes() {
        //given
        SeatCreateRequestDto seatCreateRequestDto1 = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat1 = seatService.createSeat(seatCreateRequestDto1);

        SeatCreateRequestDto seatCreateRequestDto2 = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat2 = seatService.createSeat(seatCreateRequestDto2);

        String studentId = "201811612";
        ReserveCreateRequestDto reserveCreateRequestDto1 = new ReserveCreateRequestDto(studentId, "홍길동", seat1.seatId());
        ReserveCreateRequestDto reserveCreateRequestDto2 = new ReserveCreateRequestDto(studentId, "홍길동", seat2.seatId());

        reserveService.createReserve(reserveCreateRequestDto1);

        //when, then
        assertThatThrownBy(() -> reserveService.createReserve(reserveCreateRequestDto2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s 학생은 이미 예약이 되어있습니다.", studentId));
    }

    @DisplayName("같은 좌석을 여러번 예약하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenSeatIsReservedMultipleTimes() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);
        Long seatId = seat.seatId();
        ReserveCreateRequestDto reserveCreateRequestDto1 = new ReserveCreateRequestDto("201811612", "홍길동", seatId);
        ReserveCreateRequestDto reserveCreateRequestDto2 = new ReserveCreateRequestDto("201811611", "홍길동", seatId);

        reserveService.createReserve(reserveCreateRequestDto1);

        //when, then
        assertThatThrownBy(() -> reserveService.createReserve(reserveCreateRequestDto2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s 좌석은 이미 예약이 되어있습니다.", seatId));
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void deleteReserve() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);

        ReserveCreateRequestDto reserveCreateRequestDto = new ReserveCreateRequestDto("201811612", "홍길동", seat.seatId());
        ReserveResponseDto reserve = reserveService.createReserve(reserveCreateRequestDto);

        //when
        reserveService.deleteReserve(reserve.reserveId());

        //then
        Optional<Reserve> foundReserve = jdbcReserveRepository.findById(reserve.reserveId());
        assertThat(foundReserve).isEmpty();
    }

    @DisplayName("예약 상태를 실패로 수정할 수 있다.")
    @Test
    void updateFailedReserveStatus() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);

        ReserveCreateRequestDto reserveCreateRequestDto = new ReserveCreateRequestDto("201811612", "홍길동", seat.seatId());
        ReserveResponseDto reserve = reserveService.createReserve(reserveCreateRequestDto);

        //when
        reserveService.updateFailedReserveStatus(reserve.reserveId());

        //then
        ReserveResponseDto foundReserve = reserveService.findReserve(reserve.reserveId());
        assertThat(foundReserve.reserveStatus()).isEqualTo(ReserveStatus.FAILED.getDescription());
    }

    @DisplayName("예약 상태를 수정할 예약이 존재하지 않으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenReserveToUpdateStatusIsNull() {
        //given
        UUID reserveId = UUID.randomUUID();

        //when, then
        assertThatThrownBy(() -> reserveService.updateFailedReserveStatus(reserveId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s 예약이 존재하지 않습니다.", reserveId));
    }

    @DisplayName("예약된 좌석을 수정할 수 있다.")
    @Test
    void updateSeat() {
        //given
        SeatCreateRequestDto seatCreateRequestDto1 = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto prevSeat = seatService.createSeat(seatCreateRequestDto1);

        SeatCreateRequestDto seatCreateRequestDto2 = new SeatCreateRequestDto(Category.CLOSED.getDescription());
        SeatResponseDto changeSeat = seatService.createSeat(seatCreateRequestDto2);

        ReserveCreateRequestDto reserveCreateRequestDto = new ReserveCreateRequestDto("201811612", "홍길동", prevSeat.seatId());
        ReserveResponseDto reserve = reserveService.createReserve(reserveCreateRequestDto);

        //when
        SeatReservationUpdateRequestDto seatReservationUpdateRequestDto = new SeatReservationUpdateRequestDto(reserve.reserveId(), changeSeat.seatId());
        reserveService.updateSeat(seatReservationUpdateRequestDto);

        //then
        ReserveResponseDto foundReserve = reserveService.findReserve(reserve.reserveId());
        assertThat(foundReserve.seatId()).isEqualTo(changeSeat.seatId());
    }

    @DisplayName("수정할 예약 좌석이 존재하지 않으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenSeatToUpdateReservationIsNull() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);

        ReserveCreateRequestDto reserveCreateRequestDto = new ReserveCreateRequestDto("201811612", "홍길동", seat.seatId());
        ReserveResponseDto reserve = reserveService.createReserve(reserveCreateRequestDto);

        long seatId = new Random().nextLong();
        SeatReservationUpdateRequestDto seatReservationUpdateRequestDto = new SeatReservationUpdateRequestDto(reserve.reserveId(), seatId);

        //when, then
        assertThatThrownBy(() -> reserveService.updateSeat(seatReservationUpdateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s번 좌석이 존재하지 않습니다.", seatId));
    }

    @DisplayName("수정할 예약이 존재하지 않으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenReserveToUpdateIsNull() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);

        UUID reserveId = UUID.randomUUID();
        SeatReservationUpdateRequestDto seatReservationUpdateRequestDto = new SeatReservationUpdateRequestDto(reserveId, seat.seatId());

        //when, then
        assertThatThrownBy(() -> reserveService.updateSeat(seatReservationUpdateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s 예약이 존재하지 않습니다.", reserveId));
    }

    @DisplayName("전체 예약을 조회할 수 있다.")
    @Test
    void findReserveList() {
        //given
        final int expectedSize = 2;

        SeatCreateRequestDto seatCreateRequestDto1 = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat1 = seatService.createSeat(seatCreateRequestDto1);

        ReserveCreateRequestDto reserveCreateRequestDto1 = new ReserveCreateRequestDto("201811612", "홍길동", seat1.seatId());
        reserveService.createReserve(reserveCreateRequestDto1);

        SeatCreateRequestDto seatCreateRequestDto2 = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat2 = seatService.createSeat(seatCreateRequestDto2);

        ReserveCreateRequestDto reserveCreateRequestDto2 = new ReserveCreateRequestDto("201811611", "홍길동", seat2.seatId());
        reserveService.createReserve(reserveCreateRequestDto2);

        //then
        List<ReserveResponseDto> reserveList = reserveService.findReserveList();

        //when
        assertThat(reserveList).hasSize(expectedSize);
    }

    @DisplayName("식별자로 예약을 조회할 수 있다.")
    @Test
    void findReserve() {
        //given
        SeatCreateRequestDto seatCreateRequestDto1 = new SeatCreateRequestDto(Category.OPEN.getDescription());
        SeatResponseDto seat1 = seatService.createSeat(seatCreateRequestDto1);

        ReserveCreateRequestDto reserveCreateRequestDto1 = new ReserveCreateRequestDto("201811612", "홍길동", seat1.seatId());
        ReserveResponseDto reserve = reserveService.createReserve(reserveCreateRequestDto1);

        //when
        ReserveResponseDto foundReserve = reserveService.findReserve(reserve.reserveId());

        //then
        assertThat(foundReserve.reserveId()).isEqualTo(reserve.reserveId());
    }

    @DisplayName("조회할 예약이 존재하지 않으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenReserveToFindIsNull() {
        //given
        UUID reserveId = UUID.randomUUID();

        //when, then
        assertThatThrownBy(() -> reserveService.findReserve(reserveId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s 예약이 존재하지 않습니다.", reserveId));
    }
}
