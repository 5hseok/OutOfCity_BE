package com.outofcity.server.service.member;

import com.outofcity.server.domain.BusinessMember;
import com.outofcity.server.dto.member.business.request.BusinessMemberRequestDto;
import com.outofcity.server.dto.member.business.response.BusinessMemberResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.repository.BusinessMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BusinessMemberService {

    private final BusinessMemberRepository businessMemberRepository;

    public BusinessMemberResponseDto registerBusiness(BusinessMemberRequestDto businessMemberRequestDto) {

        BusinessMember existedBusinessMember = businessMemberRepository.findByBusinessNumber(businessMemberRequestDto.businessNumber())
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_BUSINESS_MEMBER));

        if (existedBusinessMember != null) {
            log.info("이미 존재하는 사업자입니다. {}", businessMemberRequestDto.businessNumber());
            throw new BusinessException(ErrorMessage.DUPLICATE_BUSINESS_MEMBER);
        }

        //사업자 검증 확인
        if (!businessMemberRequestDto.validate()){
            log.info("사업자 등록정보를 확인받지 못했습니다.");
            throw new BusinessException(ErrorMessage.INVALID_BUSINESS_MEMBER);
        }

        // 사업 시작 날짜 파싱 - 이게 비어있으면 파싱이 안됨.
        LocalDate businessStartDate;
        try {
            if (businessMemberRequestDto.businessStartDate() == null) {
                throw new BusinessException(ErrorMessage.INVALID_DATE);
            }
            businessStartDate = LocalDate.parse(businessMemberRequestDto.businessStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (DateTimeParseException e) {
            throw new BusinessException(ErrorMessage.INVALID_DATE);
        }

        BusinessMember businessMember = BusinessMember.of(
                businessMemberRequestDto.businessName(),
                businessMemberRequestDto.businessNumber(),
                businessMemberRequestDto.businessAddress(),
                businessStartDate,
                businessMemberRequestDto.businessPhoneNumber(),
                businessMemberRequestDto.businessEmail()
        );

        // BusinessMember 저장 (DB 저장 오류 처리)
        try {
            businessMemberRepository.save(businessMember);
        } catch (Exception e) {
            log.error("BusinessMember 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorMessage.DATABASE_ERROR);
        }

        return new BusinessMemberResponseDto(
                businessMember.getBusinessMemberId(),
                businessMember.getName(),
                businessMember.getBusinessNumber(),
                businessMember.getAddress(),
                businessMember.getStartDate(),
                businessMember.getPhoneNumber(),
                businessMember.getEmail()
        );
    }
}
