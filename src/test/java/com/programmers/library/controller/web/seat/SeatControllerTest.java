package com.programmers.library.controller.web.seat;

import com.programmers.library.domain.seat.Category;
import com.programmers.library.dto.seat.request.SeatCreateRequestDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;
import com.programmers.library.service.seat.SeatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatController.class)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @DisplayName("seat_list 페이지를 반환한다.")
    @Test
    void seatsPage() throws Exception {
        //given
        List<SeatResponseDto> seatList = new ArrayList<>();
        when(seatService.findSeatList()).thenReturn(seatList);

        //when
        mockMvc.perform(get("/seats"))
                .andExpect(status().isOk())
                .andExpect(view().name("seat_list"))
                .andExpect(model().attributeExists("seatList"))
                .andExpect(model().attribute("seatList", seatList))
                .andDo(print());

        // Then
        verify(seatService, times(1)).findSeatList();
    }

    @DisplayName("new_seat 페이지를 반환한다.")
    @Test
    void newSeatPage() throws Exception {
        //nothing given

        //when, then
        mockMvc.perform(get("/new-seat"))
                .andExpect(status().isOk())
                .andExpect(view().name("new_seat"))
                .andDo(print());
    }

    @DisplayName("/seats로 리다이랙트 된다.")
    @Test
    void newSeat() throws Exception {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(Category.OPEN.getDescription());

        //when
        mockMvc.perform(post("/new-seat").flashAttr("seatCreateRequestDto", seatCreateRequestDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/seats"))
                .andDo(print());

        //then
        verify(seatService, times(1)).createSeat(seatCreateRequestDto);
    }
}
