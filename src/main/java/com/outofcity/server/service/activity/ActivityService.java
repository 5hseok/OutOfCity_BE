package com.outofcity.server.service.activity;

import com.outofcity.server.domain.Activity;
import com.outofcity.server.domain.ActivityType;
import com.outofcity.server.domain.Review;
import com.outofcity.server.domain.Type;
import com.outofcity.server.dto.activity.request.ActivityTypeRequestDto;
import com.outofcity.server.dto.activity.response.ActivityResponseDto;
import com.outofcity.server.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ReviewRepository reviewRepository;
    private final ReserveRepository reserveRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final TypeRepository typeRepository;
    private final ReserveDateRepository reserveDateRepository;

    public List<ActivityResponseDto> getTypePopularActivities(ActivityTypeRequestDto requestDto) {

        //타입과 지역, 날짜에 맞는 액티비티 중 별점이 4.5이상 이고, 리뷰가 많고, 예약자수가 많은 순서대로 반환 (최대 5개)
        Type getType = typeRepository.findByName(requestDto.type());

        //타입에 맞는 타입 액티비티 리스트 조회
        List<ActivityType> activityTypeList = activityTypeRepository.findAllByType(getType);

        // 각 타입 액티비티 항목에서 액티비티 목록 조회
        List<Activity> activityList = activityTypeList.stream()
                //액티비티를 가져오기
                .map(ActivityType::getActivity)
                //위치가 같은 액티비티들만 모음
                .filter(activity -> activity.getAddress().equals(requestDto.location()))
                //예약 가능 날짜가 있는 애들만 모음
                .filter(activity -> reserveDateRepository.findAllByActivity(activity).stream()
                        .anyMatch(reserve -> !reserve.getReserveDate().isBefore(ChronoLocalDate.from(requestDto.startDate())) &&
                                !reserve.getReserveDate().isAfter(ChronoLocalDate.from(requestDto.endDate()))))
                .toList();

        //인기순
        return activityList.stream()
                .filter(activity -> calculateAverageRating(activity) >= 4.5)
                .sorted(Comparator.comparingInt(this::getReviewCount).reversed()
                        .thenComparingInt(this::getReservationCount).reversed())
                .limit(5)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 별점 평균 계산
    private double calculateAverageRating(Activity activity) {
        List<Review> reviews = reviewRepository.findAllByActivity(activity);
        return reviews.isEmpty() ? 0.0 : reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }

    // 리뷰 개수 반환
    private int getReviewCount(Activity activity) {
        return reviewRepository.findAllByActivity(activity).size();
    }

    // 예약 개수 반환 (추가로 해당 메서드에 맞게 Activity 클래스에서 적절한 참조 추가 필요)
    private int getReservationCount(Activity activity) {
        return reserveRepository.findAllByActivity(activity).size();
    }

    private ActivityResponseDto convertToDto(Activity activity) {
        return ActivityResponseDto.of(
                activity.getActivityId(),
                activity.getName(),
                activity.getActivityPhoto(),
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
