package com.outofcity.server.dto.activity.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ActivityTypeRequestDto(
        String location,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String type
) {
}
