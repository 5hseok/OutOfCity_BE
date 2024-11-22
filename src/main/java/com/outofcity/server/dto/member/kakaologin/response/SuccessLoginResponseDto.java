package com.outofcity.server.dto.member.kakaologin.response;

import lombok.*;

@Builder
public record SuccessLoginResponseDto (
        Long id,
        String name,
        String nickname,
        String rank,
        String profileImageUrl,
        String email,
        String jwtToken
){
}
