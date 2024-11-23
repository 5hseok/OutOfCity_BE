package com.outofcity.server.dto.activity.request;

import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ActivityCartRequestDto(
        LocalDate date,       // 예약 날짜
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,       // 예약 시간 (HH:MM 형식)
        Integer participants, // 예약할 참가자 수
        Integer price         // 액티비티 가격
) {
    public static ActivityCartRequestDto of(
            LocalDate date,
            LocalTime time,
            Integer participants,
            Integer price
    ) {
        return new ActivityCartRequestDto(
                date,
                time,
                participants,
                price
        );
    }
}
