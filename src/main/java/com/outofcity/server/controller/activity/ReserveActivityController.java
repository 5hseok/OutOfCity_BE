package com.outofcity.server.controller.activity;

import com.outofcity.server.dto.activity.response.ActivityReserveResponseDto;
import com.outofcity.server.dto.activity.response.ReserveActivityResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.activity.ReserveActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ReserveActivityController {

    private final ReserveActivityService reserveActivityService;
//    private final ActivityReserveService activityReserveService;


    @GetMapping("/reservations")
    public ResponseEntity<SuccessStatusResponse<List<ReserveActivityResponseDto>>> getReservations(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.RESERVE_ACTIVITY_READ_SUCCESS, reserveActivityService.getReservations(token)));
    }

//    @PostMapping("/reserve")
//    public ResponseEntity<SuccessStatusResponse<ReserveActivityResponseDto>> createReservation(
//            @RequestHeader("Authorization") String token,
//            @RequestBody ActivityReserveResponseDto activityReserveResponseDto) {
//        ActivityReserveResponseDto actvityreserveDto = activityReserveService.createReservation(token, activityReserveResponseDto);
//        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.RESERVE_ACTIVITY_READ_SUCCESS, activityReserveService.createReservation(token, activityReserveResponseDto)));
//    }
}
