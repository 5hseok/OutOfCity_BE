package com.outofcity.server.dto.activity.response;

import java.time.LocalDateTime;

public record ActivityResponseDto(
        Long id,
        String activityName,
        String activityPhoto,
        String description,
        String state,
        Integer price,
        String mainCategory,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String address,
        Double latitude,
        Double longitude
) {
    public static ActivityResponseDto of(Long id, String activityName, String activityPhoto, String description, String state, Integer price, String mainCategory, LocalDateTime createdAt, LocalDateTime updatedAt, String address, Double latitude, Double longitude) {
        return new ActivityResponseDto(id, activityName, activityPhoto, description, state, price, mainCategory, createdAt, updatedAt, address, latitude, longitude);
    }
}
