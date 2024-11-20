package com.outofcity.server.global.exception.dto.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class KakaoLoginResponse {
    private final Long id;
    private final String name;
    private final String rank;
    private final String profileImageUrl;
    private final String email;

    public static KakaoLoginResponse of(Long id, String name, String rank, String profileImageUrl, String email) {
        return new KakaoLoginResponse(id, name, rank, profileImageUrl, email);
    }
}
