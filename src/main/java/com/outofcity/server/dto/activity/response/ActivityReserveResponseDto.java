package com.outofcity.server.dto.activity.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ActivityReserveResponseDto(
        Long userId,
        Long reservationId,
        Long activityId,
        LocalDate date,
        LocalTime time,
        Integer participants,
        Integer remainingParticipants,
        String reserveState,
        LocalDateTime createdAt
) {
    public static ActivityReserveResponseDto of(
            Long userId,
            Long reservationId,
            Long activityId,
            LocalDate date,
            LocalTime time,
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
                participants,
                remainingParticipants,
                reserveState,
                createdAt
        );
    }
}