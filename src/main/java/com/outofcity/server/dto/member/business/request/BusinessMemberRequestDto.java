package com.outofcity.server.dto.member.business.request;

public record BusinessMemberRequestDto (
    String businessName,
    Long businessNumber,
    String businessAddress,
    String businessStartDate,
    String businessPhoneNumber,
    String businessEmail,
    Boolean validate
){
    public static BusinessMemberRequestDto of(String businessName, Long businessNumber, String businessAddress, String businessStartDate, String businessPhoneNumber, String businessEmail, Boolean validate) {
        return new BusinessMemberRequestDto(businessName, businessNumber, businessAddress, businessStartDate, businessPhoneNumber, businessEmail, validate);
    }
}
