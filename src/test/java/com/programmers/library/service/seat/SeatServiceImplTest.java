package com.programmers.library.service.seat;

import com.programmers.library.domain.seat.Category;
import com.programmers.library.domain.seat.Seat;
import com.programmers.library.domain.seat.SeatStatus;
import com.programmers.library.dto.seat.request.SeatCreateRequestDto;
import com.programmers.library.dto.seat.request.SeatUpdateRequestDto;
import com.programmers.library.dto.seat.response.PageSeatResponseDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;
import com.programmers.library.repository.seat.JdbcSeatRepository;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class SeatServiceImplTest {

    private static final String CATEGORY_CLOSED = "폐쇠형";
    private static final String CATEGORY_OPEN = "개방형";

    @Autowired
    private SeatService seatService;

    @Autowired
    private JdbcSeatRepository jdbcSeatRepository;

    @DisplayName("좌석을 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"폐쇠형", "개방형"})
    void createSeat(String category) {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(category);

        //when
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);

        //then
        SeatResponseDto createdSeat = seatService.findSeat(seat.seatId());
        assertThat(createdSeat.seatId()).isEqualTo(seat.seatId());
    }

    @DisplayName("좌석을 삭제할 수 있다.")
    @Test
    void deleteSeat() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(CATEGORY_OPEN);
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);

        //when
        seatService.deleteSeat(seat.seatId());

        //then
        Optional<Seat> foundSeat = jdbcSeatRepository.findById(seat.seatId());
        assertThat(foundSeat).isEmpty();
    }

    @DisplayName("좌석 상태를 수정할 수 있다.")
    @Test
    void updateSeatStatus() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(CATEGORY_OPEN);
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);
        String prevSeatStatus = seat.seatStatus();

        //when
        seatService.updateSeatStatus(seat.seatId());

        //then
        Condition<String> changeSeatStatusCondition = new Condition<>(seatStatus -> !seatStatus.equals(prevSeatStatus), "success changing seat status");

        SeatResponseDto updatedSeat = seatService.findSeat(seat.seatId());
        assertThat(updatedSeat.seatStatus()).is(changeSeatStatusCondition);
    }

    @DisplayName("좌석 상태를 수정할 좌석이 존재하지 않으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenSeatToUpdateSeatStatusIsNull() {
        //given
        Long seatId = new Random().nextLong();

        //when, then
        assertThatThrownBy(() -> seatService.updateSeatStatus(seatId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s번 좌석이 존재하지 않습니다.", seatId));
    }

    @DisplayName("좌석 종류를 수정할 수 있다.")
    @Test
    void updateSeatCategory() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(CATEGORY_OPEN);
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);

        //when
        SeatUpdateRequestDto seatUpdateRequestDto = new SeatUpdateRequestDto(seat.seatId(), CATEGORY_CLOSED);
        seatService.updateSeatCategory(seatUpdateRequestDto);

        //then
        SeatResponseDto updatedSeat = seatService.findSeat(seat.seatId());
        assertThat(updatedSeat.category()).isEqualTo(CATEGORY_CLOSED);
    }

    @DisplayName("좌석 종류를 수정할 좌석이 존재하지 않으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenSeatToUpdateSeatCategoryIsNull() {
        //given
        Long seatId = new Random().nextLong();
        SeatUpdateRequestDto seatUpdateRequestDto = new SeatUpdateRequestDto(seatId, CATEGORY_CLOSED);

        //when, then
        assertThatThrownBy(() -> seatService.updateSeatCategory(seatUpdateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s번 좌석이 존재하지 않습니다.", seatId));
    }

    @DisplayName("전체 좌석을 조회할 수 있다.")
    @Test
    void findSeatList() {
        //given
        final int expectedSize = 2;

        SeatCreateRequestDto seatCreateRequestDto1 = new SeatCreateRequestDto(CATEGORY_OPEN);
        seatService.createSeat(seatCreateRequestDto1);

        SeatCreateRequestDto seatCreateRequestDto2 = new SeatCreateRequestDto(CATEGORY_CLOSED);
        seatService.createSeat(seatCreateRequestDto2);

        //when
        List<SeatResponseDto> seatList = seatService.findSeatList();

        //then
        assertThat(seatList).hasSize(expectedSize);
    }

    @DisplayName("카테고리별로 좌석을 조회할 수 있다.")
    @ParameterizedTest
    @EnumSource(Category.class)
    void findSeatListByCategory(Category category) {
        //given
        int expectedSize = 1;

        SeatCreateRequestDto seatCreateRequestDto1 = new SeatCreateRequestDto(CATEGORY_OPEN);
        seatService.createSeat(seatCreateRequestDto1);

        SeatCreateRequestDto seatCreateRequestDto2 = new SeatCreateRequestDto(CATEGORY_CLOSED);
        seatService.createSeat(seatCreateRequestDto2);

        //when
        List<SeatResponseDto> seatList = seatService.findSeatListByCategory(category.getDescription());

        //then
        assertThat(seatList).hasSize(expectedSize);
        assertThat(seatList.stream().allMatch(seat -> seat.category() == category.getDescription())).isTrue();

    }

    @DisplayName("식별자로 좌석을 조회할 수 있다.")
    @Test
    void findSeat() {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(CATEGORY_OPEN);
        SeatResponseDto seat = seatService.createSeat(seatCreateRequestDto);

        //when
        SeatResponseDto foundSeat = seatService.findSeat(seat.seatId());

        //then
        assertThat(foundSeat.seatId()).isEqualTo(seat.seatId());
    }

    @DisplayName("조회할 좌석이 존재하지 않으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenSeatToFindIsNull() {
        //given
        Long seatId = new Random().nextLong();

        //when, then
        assertThatThrownBy(() -> seatService.findSeat(seatId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s번 좌석이 존재하지 않습니다.", seatId));
    }

    @DisplayName("페이지별로 좌석과 전체 페이지수를 조회할 수 있다.")
    void findSeatListByPage() {
        //given
        final int pageSize = 2;
        final int pageNumber = 2;

        Seat seat1 = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat1);

        Seat seat2 = new Seat(LocalDateTime.now(), Category.OPEN, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat2);

        Seat seat3 = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        jdbcSeatRepository.insert(seat3);

        //when
        PageSeatResponseDto seatListByPage = seatService.findSeatListByPage(pageNumber, pageSize);

        //then
        final int expectedSize = 1;
        int totalPages = jdbcSeatRepository.findAll().size();

        assertThat(seatListByPage.seats()).hasSize(expectedSize);
        assertThat(seatListByPage.totalPages()).isEqualTo(totalPages);
    }
}
