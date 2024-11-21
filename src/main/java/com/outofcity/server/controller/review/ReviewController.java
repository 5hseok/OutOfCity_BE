package com.outofcity.server.controller.review;

import com.outofcity.server.dto.review.request.ReviewRequestDto;
import com.outofcity.server.dto.review.response.ReviewResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 추가
    @PostMapping("/{activityId}/reviews")
    public ResponseEntity<SuccessStatusResponse<ReviewResponseDto>> addReview(@RequestHeader("Authorization") String token, @PathVariable Long activityId, @RequestBody ReviewRequestDto reviewRequestDto) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.REVIEW_ADD_SUCCESS, reviewService.addReview(token, activityId, reviewRequestDto)
                )
        );
    }
}
