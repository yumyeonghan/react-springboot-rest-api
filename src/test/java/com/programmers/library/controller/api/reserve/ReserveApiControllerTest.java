package com.programmers.library.controller.api.reserve;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.library.domain.reserve.Reserve;
import com.programmers.library.domain.reserve.ReserveStatus;
import com.programmers.library.domain.reserve.StudentId;
import com.programmers.library.domain.seat.Category;
import com.programmers.library.domain.seat.Seat;
import com.programmers.library.domain.seat.SeatStatus;
import com.programmers.library.dto.reserve.request.ReserveCreateRequestDto;
import com.programmers.library.dto.reserve.response.ReserveResponseDto;
import com.programmers.library.service.reserve.ReserveService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReserveApiController.class)
class ReserveApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ReserveService reserveService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("예약을 생성할 수 있다.")
    @Test
    void createReserve() throws Exception {
        //given
        ReserveCreateRequestDto reserveCreateRequestDto = new ReserveCreateRequestDto("201811612", "유명한", 1l);
        String requestBody = objectMapper.writeValueAsString(reserveCreateRequestDto);

        Seat seat = new Seat(LocalDateTime.now(), Category.CLOSED, SeatStatus.RESERVATION_POSSIBLE, LocalDateTime.now());
        ReserveResponseDto reserveResponseDto = ReserveResponseDto.from(new Reserve(UUID.randomUUID(), new StudentId("201811612"), "홍길동", seat, ReserveStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now()));

        when(reserveService.createReserve(reserveCreateRequestDto)).thenReturn(reserveResponseDto);

        //when
        mockMvc.perform(post("/api/v1/reserves").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reserveId").isNotEmpty())
                .andExpect(jsonPath("$.studentId").value("201811612"))
                .andExpect(jsonPath("$.studentName").value("홍길동"))
                .andExpect(jsonPath("$.seatId").value(1l))
                .andExpect(jsonPath("$.reserveStatus").value(ReserveStatus.COMPLETED.getDescription()))
                .andDo(print());

        //then
        verify(reserveService, times(1)).createReserve(reserveCreateRequestDto);
    }
}
