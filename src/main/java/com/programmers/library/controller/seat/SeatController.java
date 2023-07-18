package com.programmers.library.controller.seat;

import com.programmers.library.dto.seat.request.SeatCreateRequestDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;
import com.programmers.library.service.seat.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/seats")
    public String seatsPage(Model model) {
        List<SeatResponseDto> seatList = seatService.findSeatList();
        model.addAttribute("seatList", seatList);
        return "seat_list";
    }

    @GetMapping("/new-seat")
    public String newSeatPage() {
        return "new_seat";
    }

    @PostMapping("/new-seat")
    public String newSeat(SeatCreateRequestDto seatCreateRequestDto) {
        seatService.createSeat(seatCreateRequestDto);
        return "redirect:/seats";
    }
}
