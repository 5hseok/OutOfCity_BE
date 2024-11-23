package com.outofcity.server.dto.admin.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ActivityRegisterRequestDto(
        Long businessMemberId,
        Long businessMemberNumber,
        String name,
        String description,
        List<String> activityPhotos,
        String state,
        Integer price,
        String mainCategory,
        List<String> subCategory,
        String location,
        Double latitude,
        Double longitude,
        List<String> activityType,
        List<ReserveEnableDate> reserveEnableDates // 예약 가능한 날짜 리스트
) {
    public record ReserveEnableDate(
            LocalDate date, // 예약 가능한 날짜
            List<ReserveTime> reserveTimes // 해당 날짜의 예약 가능한 시간 리스트
    ) {
        public record ReserveTime(
                LocalTime time, // 예약 가능한 시간
                int availableSlots // 해당 시간의 예약 가능한 인원
        ) {
        }
    }
}
