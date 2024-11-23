package com.outofcity.server.service.admin;

import com.outofcity.server.domain.*;
import com.outofcity.server.dto.admin.request.ActivityRegisterRequestDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.repository.BusinessMemberRepository;
import com.outofcity.server.repository.GeneralMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminActivityService {

    private final GeneralMemberRepository generalMemberRepository;
    private final BusinessMemberRepository businessMemberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void registerActivity(String token, ActivityRegisterRequestDto requestDto) {

        //관리자임을 확인
        GeneralMember generalMember = generalMemberRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER));

        BusinessMember businessMember = businessMemberRepository.findByBusinessNumber(requestDto.businessMemberNumber())
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_BUSINESS_MEMBER));

        //액티비티 만들기
        Activity activity = Activity.builder()
                .businessMember(businessMember)
                .name(requestDto.name())
                .description(requestDto.description())
                .address(requestDto.location())
                .latitude(requestDto.latitude())
                .longitude(requestDto.longitude())
                .price(requestDto.price())
                .state("activate")
                .mainCategory(requestDto.mainCategory())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        //액티비티 이미지 만들기
        List<ActivityImage> activityImages = requestDto.activityPhotos().stream()
                .map(url -> ActivityImage.builder()
                        .activity(activity)
                        .imageUrl(url)
                        .build())
                .toList();

        // 서브 카테고리 만들기
        List<SubCategory> subCategoryList = requestDto.subCategory().stream()
                .map(SubCategory::of)
                .collect(Collectors.toList());

        Map<String, SubCategory> subCategoryMap = subCategoryList.stream()
                .collect(Collectors.toMap(SubCategory::getName, Function.identity()));

        List<ActivitySubCategories> subCategories = requestDto.subCategory().stream()
                .map(name -> ActivitySubCategories.builder()
                        .activity(activity)
                        .subCategory(Optional.ofNullable(subCategoryMap.get(name))
                                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_SUB_CATEGORY)))
                        .build())
                .toList();

        //액티비티 타입 만들기
        List<Type> typeList = requestDto.activityType().stream()
                .map(Type::of)
                .toList();

        Map<String, Type> typeMap = typeList.stream()
                .collect(Collectors.toMap(Type::getName, Function.identity()));

        List<ActivityType> types = requestDto.subCategory().stream()
                .map(name -> ActivityType.builder()
                        .activity(activity)
                        .type(Optional.ofNullable(typeMap.get(name))
                                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_SUB_CATEGORY)))
                        .build())
                .toList();
        //액티비티 예약 가능한 날 만들기

        //액티비티 예약 가능한 시간 만들기

        //액티비티 예약 가능한 인원 만들기


    }
}
