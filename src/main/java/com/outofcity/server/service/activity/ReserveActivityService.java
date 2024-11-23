package com.outofcity.server.service.activity;

import com.outofcity.server.domain.ActivityImage;
import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.domain.Reserve;
import com.outofcity.server.domain.Review;
import com.outofcity.server.dto.activity.response.CompletedActivityResponseDto;
import com.outofcity.server.dto.activity.response.ReserveActivityResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.repository.ActivityImageRepository;
import com.outofcity.server.repository.GeneralMemberRepository;
import com.outofcity.server.repository.ReserveRepository;
import com.outofcity.server.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReserveActivityService {

    private final ReserveRepository reserveRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GeneralMemberRepository generalMemberRepository;
    private final ReviewRepository reviewRepository;
    private final ActivityImageRepository activityImageRepository;

    // 예약 목록 조회
    public List<ReserveActivityResponseDto> getReservations(String token) {
        // 토큰으로 일반회원 조회
        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        // 일반회원의 예약 목록 조회
        List<Reserve> reserveList = reserveRepository.findAllByGeneralMember(generalMember);

        // 각 예약에서 ReserveActivityResponseDto로 변환
        return reserveList.stream()
                .filter(reserve -> "reserved".equals(reserve.getReserveState()))
                .map(this::convertToDtoWithReserveNumber)
                .collect(Collectors.toList());
    }

    // 완료된 예약 목록 조회
    public List<CompletedActivityResponseDto> getCompletedReservations(String token) {
        // 토큰으로 일반회원 조회
        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        // 일반회원의 예약 목록 조회
        List<Reserve> reserveList = reserveRepository.findAllByGeneralMember(generalMember);

        // 각 예약에서 ReserveActivityResponseDto로 변환
        return reserveList.stream()
                .filter(reserve -> "completed".equals(reserve.getReserveState()))
                .map(this::convertToDtoToActivity)
                .collect(Collectors.toList());
    }

    private CompletedActivityResponseDto convertToDtoToActivity(Reserve reserve) {
        // Reserve와 연결된 Activity에 대해 리뷰 목록 조회
        List<Review> reviews = reviewRepository.findAllByActivity(reserve.getActivity());

        // 리뷰 리스트를 DTO 형식으로 변환
        List<CompletedActivityResponseDto.Review> reviewDtos = reviews.stream()
                .map(review -> new CompletedActivityResponseDto.Review(
                        review.getReviewId(),
                        new CompletedActivityResponseDto.Review.Writer(review.getGeneralMember().getGeneralMemberId(), review.getGeneralMember().getName()),
                        review.getLocation(),
                        review.getService(),
                        review.getInterest(),
                        review.getPrice(),
                        review.getRating(),
                        review.getContent(),
                        review.getLikes(),
                        review.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());

        List<ActivityImage> activityImages = activityImageRepository.findAllByActivity(reserve.getActivity());

        // CompletedActivityResponseDto 생성 후 반환
        return new CompletedActivityResponseDto(
                reserve.getActivity().getName(),
                reserve.getReservedAt().toString(),  // 필요한 형식으로 변환
                reserve.getReservedParticipants(),
                activityImages.stream().map(ActivityImage::getImageUrl).collect(Collectors.toList()),
                !reviewDtos.isEmpty(),
                reviewDtos
        );
    }


    private ReserveActivityResponseDto convertToDtoWithReserveNumber(Reserve reserve) {
        // DateTimeFormatter를 사용하여 하이픈 없는 포맷 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = reserve.getReservedAt().toLocalDate().format(formatter);

        // reserveId를 8자리로 포맷 (예: 2 -> 00000002)
        String formattedReserveId = String.format("%08d", reserve.getReserveId());

        // reserveNumber 생성: formattedDate + "-" + formattedReserveId
        String reserveNumber = formattedDate + "-" + formattedReserveId;

        return convertToDto(reserve, reserveNumber);
    }

    private ReserveActivityResponseDto convertToDto(Reserve reserve, String reserveNumber) {
        List<ActivityImage> activityImages = activityImageRepository.findAllByActivity(reserve.getActivity());

        return ReserveActivityResponseDto.of(
                reserve.getReserveId(),
                reserveNumber, // 주문번호
                reserve.getActivity().getName(),
                reserve.getReservedAt(),
                reserve.getReservedParticipants(),
                activityImages.stream().map(ActivityImage::getImageUrl).collect(Collectors.toList())
        );
    }
}

