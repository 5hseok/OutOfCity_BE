package com.outofcity.server.service.activity;

import com.outofcity.server.domain.*;
import com.outofcity.server.dto.activity.request.ActivityTypeRequestDto;
import com.outofcity.server.dto.activity.response.ActivityDetailResponseDto;
import com.outofcity.server.dto.activity.response.ActivityResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.Collections;
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
    private final ActivityRepository activityRepository;
    private final ActivityImageRepository activityImageRepository;
    private final ReserveTimeRepository reserveTimeRepository;
    private final ReserveParticipantsRepository reserveParticipantsRepository;

    //상세 조회
    public ActivityDetailResponseDto getActivity(String token, Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.ACTIVITY_NOT_FOUND));

        //액티비티 이미지 조회
        List<ActivityImage> activityImages = activityImageRepository.findAllByActivity(activity);
        log.info("activityImages: {}", activityImages);

        //Review 조회
        List<Review> reviews = reviewRepository.findAllByActivity(activity);
        log.info("reviews: {}", reviews);

        //서브 카테고리 조회
        List<ActivityType> activityTypes = activityTypeRepository.findAllByActivity(activity);
        log.info("activityTypes: {}", activityTypes);

// 예약 가능 날짜 조회
        List<ReserveDate> reserveDates = reserveDateRepository.findAllByActivity(activity);

// 예약 가능 시간 조회
        List<ReserveTime> reserveTimes = reserveDates.stream()
                .flatMap(reserveDate -> reserveTimeRepository.findAllByReserveDate(reserveDate).stream())
                .toList();

// 예약 가능 인원 조회
        List<ReserveParticipants> reserveParticipants = reserveTimes.stream()
                .flatMap(reserveTime -> reserveParticipantsRepository.findByReserveTime(reserveTime).stream())
                .toList();

        return convertToDetailDto(activity, activityImages, reviews, activityTypes, reserveDates, reserveTimes, reserveParticipants);
    }

    public List<ActivityResponseDto> getTypePopularActivities(ActivityTypeRequestDto requestDto) {

        //타입과 지역, 날짜에 맞는 액티비티 중 별점이 4.5이상 이고, 리뷰가 많고, 예약자수가 많은 순서대로 반환 (최대 5개)
        Type getType = typeRepository.findByName(requestDto.type())
                .orElseThrow(() -> new BusinessException(ErrorMessage.TYPE_NOT_EXIST));

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
                        .thenComparingInt(activity -> getReservationCount(activity, false)).reversed())
                .limit(5)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityResponseDto> getPopularActivities() {
        try {
            List<Activity> activityList = activityRepository.findAll();
            // 별점이 4.5 이상이고, 최근 일주일 내에 예약 수가 많은 순서대로 반환 (최대 5개)
            return activityList.stream()
                    .filter(activity -> calculateAverageRating(activity) >= 4.5)
                    .sorted(Comparator.comparingInt((Activity activity) -> getReservationCount(activity, true)).reversed())
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("인기 액티비티 조회 중 오류 발생", e);
            return null;
        }
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
    private int getReservationCount(Activity activity, Boolean flag) {
        //flag가 true면 인기 액티비티 조회, false면 타입에 맞는 인기 액티비티 조회
        //true는 최근 일주일 동안의 예약 수를 고려함.
        if (flag) {
            List<Reserve> reserveActivityList = reserveRepository.findAllByActivity(activity);
            return reserveActivityList.stream()
                    .filter(reserve -> reserve.getReserveDate().getReserveDate().isBefore(LocalDate.now().plusDays(1)))
                    .filter(reserve -> reserve.getReserveTime().getReserveTime().isBefore(LocalTime.now()))
                    .filter(reserve -> reserve.getReserveDate().getReserveDate().isAfter(LocalDate.now().minusDays(7)))
                    .toList().size();
        }
        return reserveRepository.findAllByActivity(activity).size();
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

    // 액티비티 상세 조회 DTO 변환
    private ActivityDetailResponseDto convertToDetailDto(
            Activity activity,
            List<ActivityImage> activityImages,
            List<Review> reviews,
            List<ActivityType> activityTypes,
            List<ReserveDate> reserveDates,
            List<ReserveTime> reserveTimes,
            List<ReserveParticipants> reserveParticipants
    ) {
        // 액티비티 이미지 URL 리스트 변환
        List<String> activityPhotos = activityImages.stream()
                .map(ActivityImage::getImageUrl)
                .toList();

        // 리뷰 리스트 변환
        List<ActivityDetailResponseDto.Review> reviewList = reviews.stream()
                .map(review -> new ActivityDetailResponseDto.Review(
                        review.getReviewId(),
                        review.getGeneralMember().getNickname(),
                        review.getRating(),
                        review.getContent(),
                        review.getLikes(),
                        review.getCreatedAt()
                ))
                .toList();

        // 리뷰 평균 계산
        double locationAverage = reviews.stream().mapToDouble(Review::getLocation).average().orElse(0.0);
        double serviceAverage = reviews.stream().mapToDouble(Review::getService).average().orElse(0.0);
        double interestAverage = reviews.stream().mapToDouble(Review::getInterest).average().orElse(0.0);
        double priceAverage = reviews.stream().mapToDouble(Review::getPrice).average().orElse(0.0);
        double ratingAverage = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);

        // 예약 가능 날짜 리스트 변환
        List<ActivityDetailResponseDto.AvailableDate> availableDates = reserveDates.stream()
                .map(reserveDate -> {
                    List<ActivityDetailResponseDto.AvailableDate.AvailableTime> availableTimes = reserveTimes.stream()
                            .filter(reserveTime -> reserveTime.getReserveDate().equals(reserveDate))
                            .map(reserveTime -> {
                                int remainParticipants = reserveParticipants.stream()
                                        .filter(reserveParticipant -> reserveParticipant.getReserveTime().equals(reserveTime))
                                        .mapToInt(ReserveParticipants::getRemainParticipants)
                                        .sum();
                                int maxParticipants = reserveParticipants.stream()
                                        .filter(reserveParticipant -> reserveParticipant.getReserveTime().equals(reserveTime))
                                        .mapToInt(ReserveParticipants::getMaxParticipants)
                                        .sum();
                                return new ActivityDetailResponseDto.AvailableDate.AvailableTime(
                                        reserveTime.getReserveTime(),
                                        remainParticipants,
                                        maxParticipants
                                );
                            })
                            .collect(Collectors.toList());

                    return new ActivityDetailResponseDto.AvailableDate(
                            reserveDate.getReserveDate(),
                            availableTimes
                    );
                })
                .toList();

        return new ActivityDetailResponseDto(
                activity.getActivityId(),
                activity.getName(),
                activityPhotos,
                activity.getDescription(),
                activity.getState(),
                activity.getPrice(),
                activity.getMainCategory(),
                activityTypes.stream()
                        .map(ActivityType::getType)
                        .map(Type::getName)
                        .collect(Collectors.toList()),
                reviews.size(),
                (int) locationAverage,
                (int) serviceAverage,
                (int) interestAverage,
                (int) priceAverage,
                ratingAverage,
                activity.getAddress(),
                activity.getLatitude(),
                activity.getLongitude(),
                availableDates,
                reviewList,
                activity.getCreatedAt(),
                activity.getUpdatedAt()
        );
    }

    public List<ActivityResponseDto> getRecommendActivities(String token, int mainCategoryId) {

        MainCategory mainCategory = Arrays.stream(MainCategory.values())
                .filter(category -> category.getId() == mainCategoryId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid main category ID: " + mainCategoryId));

        // 해당 메인 카테고리에 맞는 액티비티 중 랜덤하게 5개 반환
        List<Activity> activityList = activityRepository.findAllByMainCategory(mainCategory.getDescription());
        Collections.shuffle(activityList);

        return activityList.stream()
                .limit(5)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
