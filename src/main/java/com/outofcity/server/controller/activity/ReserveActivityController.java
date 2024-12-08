package com.outofcity.server.controller.activity;

import com.outofcity.server.dto.activity.response.ActivityReserveRequestDto;
import com.outofcity.server.dto.activity.response.ActivityReserveResponseDto;
import com.outofcity.server.dto.activity.response.CompletedActivityResponseDto;
import com.outofcity.server.dto.activity.response.ReserveActivityResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.activity.ActivityReserveService;
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

    private final ActivityReserveService activityReserveService;


    // 예약활동 조회
    @GetMapping("/reservations")
    public ResponseEntity<SuccessStatusResponse<List<ReserveActivityResponseDto>>> getReservations(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.RESERVE_ACTIVITY_READ_SUCCESS, activityReserveService.getReservations(token)));
    }

    // 완료된 활동 조회
    @GetMapping("/completed")
    public ResponseEntity<SuccessStatusResponse<List<CompletedActivityResponseDto>>> getCompletedReservations(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.COMPLETED_ACTIVITY_READ_SUCCESS, activityReserveService.getCompletedReservations(token)));
    }

    // 예약활동 추가
    @PostMapping("/reserve")
    public ResponseEntity<SuccessStatusResponse<ActivityReserveResponseDto>> createReservation(
            @RequestHeader("Authorization") String token,
            @RequestBody ActivityReserveRequestDto activityReserveRequestDto) {

        ActivityReserveResponseDto activityReserveResponseDto = activityReserveService.createReservation(token, activityReserveRequestDto);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.RESERVE_ACTIVITY_RESERVE_SUCCESS, activityReserveResponseDto));
    }

    // 예약활동 삭제
    @DeleteMapping("/reserve/{reserveId}")
    public ResponseEntity<SuccessStatusResponse<SuccessMessage>> deleteReservation(@RequestHeader ("Authorization") String token, @PathVariable Long reserveId) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                        SuccessMessage.RESERVE_ACTIVITY_DELETE_SUCCESS, activityReserveService.deleteReservation(token, reserveId)
                )
        );
    }

}
