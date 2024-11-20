package com.outofcity.server.dto.member.business.response;

import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.exception.message.SuccessMessage;

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
