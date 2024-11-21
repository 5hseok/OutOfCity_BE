package com.outofcity.server.dto.activity.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReserveActivityResponseDto(
        Long id,
        String orderNumber,
        String orderName,
        LocalDateTime orderTime,
        Integer orderPerson,
        String orderActivityPhoto
) {
    public static ReserveActivityResponseDto of(Long id, String orderNumber, String orderName, LocalDateTime orderTime, Integer orderPerson, String orderActivityPhoto) {
        return new ReserveActivityResponseDto(id, orderNumber, orderName, orderTime, orderPerson, orderActivityPhoto);
    }
}
