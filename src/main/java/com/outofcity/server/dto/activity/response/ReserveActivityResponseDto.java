package com.outofcity.server.dto.activity.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReserveActivityResponseDto(
        Long id,
        String orderNumber,
        String orderName,
        LocalDateTime orderTime,
        Integer orderPerson,
        List<String> orderActivityPhoto
) {
    public static ReserveActivityResponseDto of(Long id, String orderNumber, String orderName, LocalDateTime orderTime, Integer orderPerson, List<String> orderActivityPhoto) {
        return new ReserveActivityResponseDto(id, orderNumber, orderName, orderTime, orderPerson, orderActivityPhoto);
    }
}
