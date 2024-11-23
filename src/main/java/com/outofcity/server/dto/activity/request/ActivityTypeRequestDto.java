package com.outofcity.server.dto.activity.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ActivityTypeRequestDto(
        String location,
        LocalDate startDate,
        LocalDate endDate,
        String type
) {
}
