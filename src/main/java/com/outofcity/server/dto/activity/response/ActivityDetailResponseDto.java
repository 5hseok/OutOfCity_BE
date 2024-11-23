package com.outofcity.server.dto.activity.response;

import com.outofcity.server.domain.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ActivityDetailResponseDto(
        Long id,
        String activityName,
        List<String> activityPhoto,
        String description,
        String state,
        Integer price,
        String mainCategory,
        String address,
        Double latitude,
        Double longitude
//        List<Review> reviews,
) {
    public record Review(
            Long id,
            Writer writer,
            Integer reviewLocation,
            Integer reviewService,
            Integer reviewInterest,
            Integer reviewPrice,
            Double reviewTotalStar,
            String reviewContent,
            Long reviewLikeCount,
            LocalDateTime createdAt
    ) {
        public record Writer(
                Long id,
                String name
        ) {
        }
    }

}
