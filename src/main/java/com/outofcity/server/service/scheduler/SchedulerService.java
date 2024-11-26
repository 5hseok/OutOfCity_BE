package com.outofcity.server.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class SchedulerService {

    @Scheduled(fixedRate = 1800000) // 30 minutes in milliseconds
    private static final void updateDB(){
        // Update the database`
        log.info("Database updated");
    }

}