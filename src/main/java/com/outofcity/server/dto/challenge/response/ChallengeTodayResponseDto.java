package com.outofcity.server.dto.challenge.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ChallengeTodayResponseDto(
        Long id,
        String content,
        LocalDate createdAt
) {
}
