package com.programmers.library.controller.seat;

import com.programmers.library.controller.web.seat.SeatController;
import com.programmers.library.domain.seat.Category;
import com.programmers.library.dto.seat.request.SeatCreateRequestDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;
import com.programmers.library.service.seat.SeatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

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
        Mockito.when(seatService.findSeatList()).thenReturn(seatList);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/seats"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("seat_list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("seatList"))
                .andExpect(MockMvcResultMatchers.model().attribute("seatList", seatList))
                .andDo(MockMvcResultHandlers.print());

        // Then
        Mockito.verify(seatService, Mockito.times(1)).findSeatList();
    }

    @DisplayName("new_seat 페이지를 반환한다.")
    @Test
    void newSeatPage() throws Exception {
        //nothing given

        //when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/new-seat"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("new_seat"))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("/seats로 리다이랙트 된다.")
    @Test
    void newSeat() throws Exception {
        //given
        SeatCreateRequestDto seatCreateRequestDto = new SeatCreateRequestDto(Category.OPEN.getDescription());

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/new-seat").flashAttr("seatCreateRequestDto", seatCreateRequestDto))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/seats"))
                .andDo(MockMvcResultHandlers.print());

        //then
        Mockito.verify(seatService, Mockito.times(1)).createSeat(seatCreateRequestDto);
    }
}
