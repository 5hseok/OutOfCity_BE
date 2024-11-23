package com.outofcity.server.dto.activity.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record ActivityDetailResponseDto(
            Long id,
            String name,
            List<String> activityPhotos,
            String description,
            String state,
            int price,
            String mainCategory,
            List<String> subCategory,
            int reviewCount,
            int locationAverage,
            int serviveAverage,
            int interestAverage,
            int priceAverage,
            double ratingAverage,
            String address,
            double latitude,
            double longitude,
            List<AvailableDate> availableDates,
            List<Review> reviews,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public record AvailableDate(
                LocalDate date,
                List<AvailableTime> times
        ) {
            public record AvailableTime(
                    LocalTime time,
                    int remainParticipants,
                    int maxParticipants
            ) {}
        }

        public record Review(
                Long reviewId,
                String reviewerName,
                double rating,
                String comment,
                Long likes,
                LocalDateTime createdAt
        ) {}
}
