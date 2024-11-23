package com.outofcity.server.dto.activity.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ActivityReserveRequestDto(
        Long activityId,
        LocalDate reserveDate,
        LocalTime reserveTime,
        Integer reserveParticipants
) {
}
