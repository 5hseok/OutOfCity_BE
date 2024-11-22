package com.outofcity.server.dto.activity.response;

import java.time.LocalDateTime;

public record ActivityReserveResponseDto(
        Long userId,
        Long reservationId,
        Long activityId,
        LocalDateTime date,
        LocalDateTime time,
        Integer price,
        Integer participants,
        Integer remainingParticipants,
        String reserveState,
        LocalDateTime createdAt
) {
    public static ActivityReserveResponseDto of(
            Long userId,
            Long reservationId,
            Long activityId,
            LocalDateTime date,
            LocalDateTime time,
            Integer price,
            Integer participants,
            Integer remainingParticipants,
            String reserveState,
            LocalDateTime createdAt
    ) {
        return new ActivityReserveResponseDto(
                userId,
                reservationId,
                activityId,
                date,
                time,
                price,
                participants,
                remainingParticipants,
                reserveState,
                createdAt
        );
    }
}
