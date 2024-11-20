package com.outofcity.server.dto.member.business.request;

public record BusinessMemberRequestDto (
    String businessName,
    Long businessNumber,
    String businessAddress,
    String businessStartDate,
    String businessPhoneNumber,
    String businessEmail
){
    public static BusinessMemberRequestDto of(String businessName, Long businessNumber, String businessAddress, String businessStartDate, String businessPhoneNumber, String businessEmail) {
        return new BusinessMemberRequestDto(businessName, businessNumber, businessAddress, businessStartDate, businessPhoneNumber, businessEmail);
    }
}
