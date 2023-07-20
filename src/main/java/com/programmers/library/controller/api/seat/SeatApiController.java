package com.programmers.library.controller.api.seat;

import com.programmers.library.dto.seat.response.PageSeatResponseDto;
import com.programmers.library.dto.seat.response.SeatResponseDto;
import com.programmers.library.service.seat.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SeatApiController {

    private final SeatService seatService;

    @GetMapping("/v1/seats")
    public List<SeatResponseDto> seatList() {
        return seatService.findSeatList();
    }

    @GetMapping("/v1/page-seats")
    public PageSeatResponseDto seatListByPage(@RequestParam(defaultValue = "1") int page, @RequestParam int size) {
        return seatService.findSeatListByPage(page, size);
    }
}
