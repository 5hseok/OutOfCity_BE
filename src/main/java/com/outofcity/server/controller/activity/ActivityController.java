package com.outofcity.server.controller.activity;

import com.outofcity.server.dto.activity.request.ActivityTypeRequestDto;
import com.outofcity.server.dto.activity.response.ActivityFavoritiesResponseDto;
import com.outofcity.server.dto.activity.response.ActivityResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.activity.ActivityFavoritiesService;
import com.outofcity.server.service.activity.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityFavoritiesService activityFavoritiesService;
    private final ActivityService activityService;

    //타입에 맞는 인기 액티비티 조회
    @GetMapping("/types")
    public ResponseEntity<SuccessStatusResponse<List<ActivityResponseDto>>> getTypePopularActivities(
            @RequestBody ActivityTypeRequestDto requestDto) {

        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.ACTIVITY_TYPE_READ_SUCCESS, activityService.getTypePopularActivities(requestDto)
        ));
    }

    // 일반 사용자 액티비티 찜 목록 조회
    @GetMapping("/favorities")
    public ResponseEntity<SuccessStatusResponse<List<ActivityResponseDto>>> getFavorites(@RequestHeader ("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.ACTIVITY_LIKE_READ_SUCCESS, activityFavoritiesService.getFavorites(token)
        ));
    }

    // 액티비티 찜 추가
    @PostMapping("/favorities/{activityId}")
    public ResponseEntity<SuccessStatusResponse<ActivityFavoritiesResponseDto>> addFavorite(@RequestHeader ("Authorization") String token, @PathVariable Long activityId) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.ACTIVITY_LIKE_ADD_SUCCESS, activityFavoritiesService.addFavorite(token, activityId)
                )
        );
    }

    // 액티비티 찜 취소
    @DeleteMapping("/favorities/{activityId}")
    public ResponseEntity<SuccessStatusResponse<SuccessMessage>> deleteFavorite(@RequestHeader ("Authorization") String token, @PathVariable Long activityId) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.ACTIVITY_LIKE_DELETE_SUCCESS, activityFavoritiesService.deleteFavorite(token, activityId)
                )
        );
    }
}
