package com.outofcity.server.service.review;

import com.outofcity.server.domain.Activity;
import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.domain.Review;
import com.outofcity.server.dto.review.request.ReviewRequestDto;
import com.outofcity.server.dto.review.response.ReviewResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.repository.ActivityRepository;
import com.outofcity.server.repository.GeneralMemberRepository;
import com.outofcity.server.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ActivityRepository activityRepository;

    public ReviewResponseDto addReview(String token, Long activityId, ReviewRequestDto reviewRequestDto) {

        // 토큰으로 일반회원 조회
        GeneralMember generalMember = generalMemberRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));
        log.info("일반회원 조회 성공");
        //activityId로 액티비티 조회
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.ACTIVITY_NOT_FOUND));
        log.info("액티비티 조회 성공");

        log.info("리뷰 요청 정보: {}", reviewRequestDto);
        //rating 정의
        Double stars = Arrays.stream(new Integer[] {
                        reviewRequestDto.location(),
                        reviewRequestDto.price(),
                        reviewRequestDto.interest(),
                        reviewRequestDto.service()
                })
                .mapToInt(Integer::intValue) // Integer 값을 int로 변환
                .average()
                .orElseThrow(() -> new BusinessException(ErrorMessage.INVALID_REVIEW));
        log.info("평점 계산 성공");
        //리뷰 생성
        Review review = Review.of(
                activity,
                generalMember,
                activity.getBusinessMember(),
                reviewRequestDto.location(),
                reviewRequestDto.service(),
                reviewRequestDto.interest(),
                reviewRequestDto.price(),
                stars,
                reviewRequestDto.content(),
                0L,
                LocalDateTime.now()
        );
        log.info("리뷰 생성 성공");

        reviewRepository.save(review);
        log.info("리뷰 저장 성공");
        return ReviewResponseDto.builder()
                .location(review.getLocation())
                .price(review.getPrice())
                .interest(review.getInterest())
                .service(review.getService())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
