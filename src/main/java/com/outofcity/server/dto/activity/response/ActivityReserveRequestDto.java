package com.outofcity.server.dto.activity.response;

import java.time.LocalDateTime;

public record ActivityReserveRequestDto(
        Long activityId,
        LocalDateTime reserveDate,
        LocalDateTime reserveTime,
        Integer reserveParticipants
) {
}
