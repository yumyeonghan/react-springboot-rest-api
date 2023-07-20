package com.programmers.library.controller.api.seat;

import com.programmers.library.domain.seat.Category;
import com.programmers.library.domain.seat.SeatStatus;
import com.programmers.library.dto.seat.response.PageSeatResponseDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;
import com.programmers.library.service.seat.SeatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatApiController.class)
class SeatApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @DisplayName("전체 좌석 목록을 조회할 수 있다.")
    @Test
    void seatList() throws Exception {
        //given
        List<SeatResponseDto> seatList = new ArrayList<>();
        when(seatService.findSeatList()).thenReturn(seatList);

        //when
        mockMvc.perform(get("/api/v1/seats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)))
                .andDo(print());

        //then
        verify(seatService, times(1)).findSeatList();
    }

    @DisplayName("페이지별로 좌석 목록과 전체 페이지 수를 조회할 수 있다.")
    @Test
    void seatListByPage() throws Exception{
        //given
        ArrayList<SeatResponseDto> seatList = new ArrayList<>();
        seatList.add(new SeatResponseDto(3l, Category.OPEN.getDescription(), SeatStatus.RESERVATION_POSSIBLE.getDescription()));
        int totalPages = 3;
        PageSeatResponseDto pageSeatResponseDto = new PageSeatResponseDto(seatList, totalPages);

        int pageNumber = 2;
        int pageSize = 2;
        when(seatService.findSeatListByPage(pageNumber, pageSize)).thenReturn(pageSeatResponseDto);

        //when
        mockMvc.perform(get("/api/v1/page-seats?page=2&size=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.seats", hasSize(1)))
                .andExpect(jsonPath("$.seats[0].seatId").value(3L))
                .andExpect(jsonPath("$.seats[0].category").value("개방형"))
                .andExpect(jsonPath("$.seats[0].seatStatus").value("예약가능"))
                .andExpect(jsonPath("$.totalPages").value(totalPages))
                .andDo(print());

        //then
        verify(seatService, times(1)).findSeatListByPage(pageNumber, pageSize);
    }
}
