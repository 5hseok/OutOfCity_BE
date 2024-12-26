package com.outofcity.server.config;

import com.outofcity.server.service.scheduler.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerConfiguration {
    private final SchedulerService schedulerService;

    @Scheduled(fixedDelay = 1800000, initialDelay = 6000)
    public void run() {
        schedulerService.updateUserReserveStatus();
    }
}
