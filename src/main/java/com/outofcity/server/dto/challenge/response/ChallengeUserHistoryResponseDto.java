package com.outofcity.server.dto.challenge.response;

import lombok.Builder;

@Builder
public record ChallengeUserHistoryResponseDto(
        Long id,
        String image_url,
        String content,
        String performedAt,
        String certification
) {
}
