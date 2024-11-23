package com.outofcity.server.dto.activity.response;

import java.time.LocalDateTime;
import java.util.List;

public record ActivityResponseDto(
        Long id,
        String activityName,
        List<String> activityPhoto,
        String description,
        String state,
        Integer price,
        String mainCategory,
        List<String> Type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String address,
        Double latitude,
        Double longitude
) {
    public static ActivityResponseDto of(Long id, String activityName, List<String> activityPhoto, String description, String state, Integer price, String mainCategory, List<String> types, LocalDateTime createdAt, LocalDateTime updatedAt, String address, Double latitude, Double longitude) {
        return new ActivityResponseDto(id, activityName, activityPhoto, description, state, price, mainCategory, types, createdAt, updatedAt, address, latitude, longitude);
    }
}
