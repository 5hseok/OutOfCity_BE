package com.outofcity.server.service.admin;

import com.outofcity.server.domain.*;
import com.outofcity.server.dto.admin.request.ActivityRegisterRequestDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ActivityRepository activityRepository;
    private final ActivityImageRepository activityImageRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ActivitySubCategoriesRepository activitySubCategoryRepository;
    private final TypeRepository typeRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final ReserveDateRepository reserveDateRepository;
    private final ReserveTimeRepository reserveTimeRepository;
    private final ReserveParticipantsRepository reserveParticipantsRepository;

    public void registerActivity(String token, ActivityRegisterRequestDto requestDto) {

        //관리자임을 확인
        GeneralMember generalMember = generalMemberRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER));

        BusinessMember businessMember = businessMemberRepository.findByBusinessNumber(requestDto.businessMemberNumber())
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_BUSINESS_MEMBER));

        try {
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

            //이미 존재하는 액티비티인지 확인
            Boolean flag = activityRepository.findByNameAndBusinessMember(requestDto.name(), businessMember)
                    .isPresent();
            if (flag) {
                throw new BusinessException(ErrorMessage.ALREADY_EXIST_ACTIVITY);
            }

            //액티비티 이미지 만들기
            List<ActivityImage> activityImages = requestDto.activityPhotos().stream()
                    .map(url -> ActivityImage.builder()
                            .activity(activity)
                            .imageUrl(url)
                            .build())
                    .toList();
            log.info("ActivityImageList: {}", activityImages.stream().map(ActivityImage::getImageUrl).collect(Collectors.toList()));

            // 서브 카테고리 만들기
            List<SubCategory> subCategoryList = requestDto.subCategory().stream()
                    .map(subCategoryName -> subCategoryRepository.findByName(subCategoryName)
                            .orElseGet(() -> {
                                SubCategory newSubCategory = new SubCategory(subCategoryName);
                                subCategoryRepository.save(newSubCategory);
                                return newSubCategory;
                            }))
                    .toList();

            log.info("SubCategoryList: {}", subCategoryList.stream().map(SubCategory::getName).collect(Collectors.toList()));

            // 액티비티 서브 카테고리 만들기
            List<ActivitySubCategories> subCategories = requestDto.subCategory().stream()
                    .map(name -> {
                        SubCategory matchedSubCategory = subCategoryList.stream()
                                .filter(subCategory -> subCategory.getName().equals(name))
                                .findFirst()
                                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_SUB_CATEGORY));
                        return ActivitySubCategories.builder()
                                .activity(activity)
                                .subCategory(matchedSubCategory)
                                .build();
                    })
                    .collect(Collectors.toList());

            // 액티비티 타입 만들기
            List<Type> typeList = requestDto.activityType().stream()
                    .map(typeName -> typeRepository.findByName(typeName)
                            .orElseThrow(() -> new BusinessException(ErrorMessage.TYPE_NOT_EXIST)))
                    .collect(Collectors.toList());

            // 액티비티 타입 만들기
            List<ActivityType> activityTypes = requestDto.activityType().stream()
                    .map(name -> {
                        Type matchedType = typeList.stream()
                                .filter(type -> type.getName().equals(name))
                                .findFirst()
                                .orElseThrow(() -> new BusinessException(ErrorMessage.TYPE_NOT_EXIST));
                        return ActivityType.builder()
                                .activity(activity)
                                .type(matchedType)
                                .build();
                    })
                    .collect(Collectors.toList());

            log.info("ActivityTypeList: {}", activityTypes.stream().map(ActivityType::getType).map(Type::getName).collect(Collectors.toList()));
            //액티비티 예약 가능한 날 만들기
            List<ReserveDate> reserveDates = requestDto.reserveEnableDates().stream()
                    .map(reserveEnableDate -> ReserveDate.builder()
                            .activity(activity)
                            .reserveDate(reserveEnableDate.date())
                            .build())
                    .toList();

            //액티비티 예약 가능한 시간 만들기
            List<ReserveTime> reserveTimes = requestDto.reserveEnableDates().stream()
                    .flatMap(reserveEnableDate -> reserveEnableDate.reserveTimes().stream()
                            .map(reserveTime -> ReserveTime.builder()
                                    .reserveDate(reserveDates.stream()
                                            .filter(reserveDate -> reserveDate.getReserveDate().equals(reserveEnableDate.date()))
                                            .findFirst()
                                            .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_RESERVE_DATE)))
                                    .reserveTime(reserveTime.time())
                                    .build()))
                    .toList();
            //액티비티 예약 가능한 인원 만들기
            List<ReserveParticipants> reserveParticipants = requestDto.reserveEnableDates().stream()
                    .flatMap(reserveEnableDate -> reserveEnableDate.reserveTimes().stream()
                            .map(reserveTime -> ReserveParticipants.builder()
                                    .reserveTime(reserveTimes.stream()
                                            .filter(reserveTime1 -> reserveTime1.getReserveTime().equals(reserveTime.time()))
                                            .findFirst()
                                            .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_RESERVE_TIME)))
                                    .maxParticipants(reserveTime.availablePersonCount())
                                    .build()))
                    .toList();

            activityRepository.save(activity);
            activityImageRepository.saveAll(activityImages);
            subCategoryRepository.saveAll(subCategoryList);
            activitySubCategoryRepository.saveAll(subCategories);
            typeRepository.saveAll(typeList);
            activityTypeRepository.saveAll(activityTypes);
            reserveDateRepository.saveAll(reserveDates);
            reserveTimeRepository.saveAll(reserveTimes);
            reserveParticipantsRepository.saveAll(reserveParticipants);
        }
        catch (Exception e) {
            log.error("액티비티 등록 실패", e);
            throw new BusinessException(ErrorMessage.FAIL_TO_REGISTER_ACTIVITY);
        }

    }
}
