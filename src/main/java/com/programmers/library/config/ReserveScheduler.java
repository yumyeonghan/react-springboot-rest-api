package com.programmers.library.config;

import com.programmers.library.service.reserve.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ReserveScheduler {

    private final ReserveService reserveService;

    @Scheduled(cron = "0 0 0 * * ?") // logic to be executed 'every day at 00:00'
    public void initReservation() {
        reserveService.deleteAllReserve();
    }
}
