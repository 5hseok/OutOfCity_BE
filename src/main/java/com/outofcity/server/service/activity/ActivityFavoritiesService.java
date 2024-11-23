package com.outofcity.server.service.activity;

import com.outofcity.server.domain.Activity;
import com.outofcity.server.domain.ActivityFavorities;
import com.outofcity.server.domain.ActivityImage;
import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.dto.activity.response.ActivityFavoritiesResponseDto;
import com.outofcity.server.dto.activity.response.ActivityResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.repository.ActivityFavoritiesRepository;
import com.outofcity.server.repository.ActivityImageRepository;
import com.outofcity.server.repository.ActivityRepository;
import com.outofcity.server.repository.GeneralMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ActivityFavoritiesService {

    private final ActivityFavoritiesRepository activityFavoritiesRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ActivityRepository activityRepository;
    private final ActivityImageRepository activityImageRepository;


    // 액티비티 즐겨찾기 목록 조회
    public List<ActivityResponseDto> getFavorites(String token) {
        // 토큰으로 일반회원 조회
        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);

        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        // 일반회원의 액티비티 즐겨찾기 목록 조회
        List<ActivityFavorities> activityFavorities = activityFavoritiesRepository.findAllByGeneralMember(generalMember);

        log.info("activityFavorities: {}", activityFavorities);

        // 각 즐겨찾기에서 액티비티 정보를 가져와 ActivityResponseDto로 변환
        return activityFavorities.stream()
                .map(ActivityFavorities::getActivity)
                .map(activity -> activityRepository.findById(activity.getActivityId())
                        .orElseThrow(() -> new BusinessException(ErrorMessage.ACTIVITY_NOT_FOUND)))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 액티비티 즐겨찾기 추가
    public ActivityFavoritiesResponseDto addFavorite(String token, Long activityId) {

        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);

        // 토큰으로 일반회원 조회
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));
        // 액티비티 조회
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.ACTIVITY_NOT_FOUND));

        // 액티비티 즐겨찾기 추가
        ActivityFavorities activityFavorities = ActivityFavorities.builder()
                .activity(activity)
                .generalMember(generalMember)
                .build();

        activityFavoritiesRepository.save(activityFavorities);

        // DTO로 변환하여 반환
        return ActivityFavoritiesResponseDto.of(
                activityFavorities.getId(),
                activityFavorities.getGeneralMember().getGeneralMemberId(),
                activityFavorities.getActivity().getActivityId()
        );
    }

    // 액티비티 즐겨찾기 삭제
    public SuccessMessage deleteFavorite(String token, Long activityId) {

        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);

        // 토큰으로 일반회원 조회
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        // 액티비티 조회
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.ACTIVITY_NOT_FOUND));

        // 액티비티 즐겨찾기 삭제
        ActivityFavorities activityFavorities = activityFavoritiesRepository.findByActivityAndGeneralMember(activity, generalMember)
                .orElseThrow(() -> new BusinessException(ErrorMessage.ACTIVITY_LIKE_NOT_FOUND));

        activityFavoritiesRepository.delete(activityFavorities);

        return SuccessMessage.ACTIVITY_LIKE_DELETE_SUCCESS;
    }

    private ActivityResponseDto convertToDto(Activity activity) {

        List<ActivityImage> activityImages = activityImageRepository.findAllByActivity(activity);
        return ActivityResponseDto.of(
                activity.getActivityId(),
                activity.getName(),
                activityImages.stream()
                        .map(ActivityImage::getImageUrl)
                        .collect(Collectors.toList()),
                activity.getDescription(),
                activity.getState(),
                activity.getPrice(),
                activity.getMainCategory(),
                activity.getCreatedAt(),
                activity.getUpdatedAt(),
                activity.getAddress(),
                activity.getLatitude(),
                activity.getLongitude()
        );
    }
}
