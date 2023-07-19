package com.programmers.library.controller.api.reserve;

import com.programmers.library.dto.reserve.request.ReserveCreateRequestDto;
import com.programmers.library.dto.reserve.response.ReserveResponseDto;
import com.programmers.library.service.reserve.ReserveService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ReserveApiController {

    private final ReserveService reserveService;

    @PostMapping("/v1/reserves")
    public ReserveResponseDto createReserve(@RequestBody ReserveCreateRequestDto reserveCreateRequestDto) {
        return reserveService.createReserve(reserveCreateRequestDto);
    }
}
