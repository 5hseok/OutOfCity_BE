package com.outofcity.server.dto.member.kakaologin.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessLoginResponseDto {

    private Long id;
    private String name;
    private String rank;
    private String profileImageUrl;
    private String email;

    @Builder
    public SuccessLoginResponseDto(Long id, String name, String rank, String profileImageUrl, String email) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
    }
}
