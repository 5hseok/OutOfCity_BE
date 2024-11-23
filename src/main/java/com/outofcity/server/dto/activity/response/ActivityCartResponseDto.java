package com.outofcity.server.dto.activity.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record ActivityCartResponseDto(
        int code,                    // 응답 코드
        String message,              // 응답 메시지
        Long cartItemId,             // 장바구니 항목 ID
        Long activityId,             // 액티비티 ID
        Long userId,                 // 사용자 ID
        LocalDate date,              // 예약 날짜
        LocalTime time,              // 예약 시간
        Integer participants,        // 예약할 참가자 수
        Integer price,               // 액티비티 가격
        Long cartId                  // 장바구니 ID
) {
    public static ActivityCartResponseDto of(
            int code,
            String message,
            Long cartItemId,
            Long activityId,
            Long userId,
            LocalDate date,
            @JsonFormat(pattern = "HH:mm")
            LocalTime time,
            Integer participants,
            Integer price,
            Long cartId
    ) {
        return new ActivityCartResponseDto(
                code,
                message,
                cartItemId,
                activityId,
                userId,
                date,
                time,
                participants,
                price,
                cartId
        );
    }
}
