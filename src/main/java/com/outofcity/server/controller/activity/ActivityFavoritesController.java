package com.outofcity.server.controller.activity;

import com.outofcity.server.dto.activity.response.ActivityFavoritiesResponseDto;
import com.outofcity.server.dto.activity.response.ActivityResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.activity.ActivityFavoritiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/activities/favorities")
public class ActivityFavoritesController {

    private final ActivityFavoritiesService activityFavoritiesService;

    @GetMapping("")
    public ResponseEntity<SuccessStatusResponse<List<ActivityResponseDto>>> getFavorites(@RequestHeader ("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.ACTIVITY_LIKE_READ_SUCCESS, activityFavoritiesService.getFavorites(token)
        ));
    }

    @PostMapping("/{activityId}")
    public ResponseEntity<SuccessStatusResponse<ActivityFavoritiesResponseDto>> addFavorite(@RequestHeader ("Authorization") String token, @PathVariable Long activityId) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.ACTIVITY_LIKE_ADD_SUCCESS, activityFavoritiesService.addFavorite(token, activityId)
                )
        );
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<SuccessStatusResponse<SuccessMessage>> deleteFavorite(@RequestHeader ("Authorization") String token, @PathVariable Long activityId) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.ACTIVITY_LIKE_DELETE_SUCCESS, activityFavoritiesService.deleteFavorite(token, activityId)
                )
        );
    }
}
