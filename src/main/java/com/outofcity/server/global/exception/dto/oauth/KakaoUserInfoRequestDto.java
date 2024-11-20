package com.outofcity.server.global.exception.dto.oauth;

import lombok.Builder;

@Builder
public record KakaoUserInfoRequestDto(
        Long id,
        String name,
        String profileImageUrl,
        String email
) {
}
