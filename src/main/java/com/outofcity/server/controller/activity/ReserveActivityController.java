package com.outofcity.server.controller.activity;

import com.outofcity.server.dto.activity.response.ActivityResponseDto;
import com.outofcity.server.dto.activity.response.CompletedActivityResponseDto;
import com.outofcity.server.dto.activity.response.ReserveActivityResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.activity.ReserveActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ReserveActivityController {

    private final ReserveActivityService reserveActivityService;

    @GetMapping("/reservations")
    public ResponseEntity<SuccessStatusResponse<List<ReserveActivityResponseDto>>> getReservations(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.RESERVE_ACTIVITY_READ_SUCCESS, reserveActivityService.getReservations(token)));
    }

    @GetMapping("/completed")
    public ResponseEntity<SuccessStatusResponse<List<CompletedActivityResponseDto>>> getCompletedReservations(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.COMPLETED_ACTIVITY_READ_SUCCESS, reserveActivityService.getCompletedReservations(token)));
    }
}
