package com.outofcity.server.controller.activity;

import com.outofcity.server.dto.activity.response.ActivityFavoritiesResponseDto;
import com.outofcity.server.dto.activity.response.ActivityReserveResDto;
import com.outofcity.server.dto.activity.response.ReserveActivityResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
//import com.outofcity.server.service.activity.ActivityReserveService;
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
//    private final ActivityReserveService activityReserveService;


    @GetMapping("/reservations")
    public ResponseEntity<SuccessStatusResponse<List<ReserveActivityResponseDto>>> getReservations(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.RESERVE_ACTIVITY_READ_SUCCESS, reserveActivityService.getReservations(token)));
    }

    @PostMapping("/reserve")
    public ResponseEntity<SuccessStatusResponse<ReserveActivityResponseDto>> createReservation(
            @RequestHeader("Authorization") String token,
            @RequestBody ActivityReserveResDto activityReserveResDto) {
        ActivityReserveResDto actvityreserveDto = activityReserveService.createReservation(token, activityReserveResDto);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.RESERVE_ACTIVITY_READ_SUCCESS, activityReserveService.createReservation(token, activityReserveResDto)));
    }
}
