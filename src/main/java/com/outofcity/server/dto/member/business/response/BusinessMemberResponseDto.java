package com.outofcity.server.dto.member.business.response;

import java.time.LocalDateTime;

public record BusinessMemberResponseDto(
        Long id,
        String businessName,
        Long businessNumber,
        String businessAddress,
        LocalDateTime businessStartDate,
        String businessPhoneNumber,
        String businessEmail
){
}
