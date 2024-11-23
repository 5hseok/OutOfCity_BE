package com.outofcity.server.dto.activity.response;

import java.time.LocalDateTime;

public record ActivityReserveResponseDto(
        Long id,
        LocalDateTime reservedDate,
        LocalDateTime reservedTime,
        Integer remainParticipants

) {
    public static ActivityReserveResponseDto of(Long id, Long activityId, String state, Integer price, LocalDateTime reservedDate, LocalDateTime reservedTime, Integer remainParticipants, Integer reservedParticipants) {
        return new ActivityReserveResponseDto(id, reservedDate, reservedTime, remainParticipants);
    }
}