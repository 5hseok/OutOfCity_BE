package com.outofcity.server.dto.challenge.request;

import lombok.Builder;

@Builder
public record ChallengeRegisterImageRequestDto(
        String imageUrl
) {
}
