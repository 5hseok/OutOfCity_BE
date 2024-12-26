package com.outofcity.server.service.activity;

import com.outofcity.server.domain.*;
import com.outofcity.server.dto.activity.response.ActivityReserveRequestDto;
import com.outofcity.server.dto.activity.response.ActivityReserveResponseDto;
import com.outofcity.server.dto.activity.response.CompletedActivityResponseDto;
import com.outofcity.server.dto.activity.response.ReserveActivityResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActivityReserveService {

    private final ReserveRepository reserveRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GeneralMemberRepository generalMemberRepository;
    private final ActivityRepository activityRepository;
    private final ReserveParticipantsRepository reserveParticipantsRepository;
    private final ReserveTimeRepository reserveTimeRepository;
    private final ReserveDateRepository reserveDateRepository;
    private final ReviewRepository reviewRepository;
    private final ActivityImageRepository activityImageRepository;


    public ActivityReserveResponseDto createReservation(String token, ActivityReserveRequestDto activityReserveRequestDto) {

        // 토큰으로 일반회원 조회
        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        Activity activity = activityRepository.findById(activityReserveRequestDto.activityId())
                .orElseThrow(() -> new BusinessException(ErrorMessage.ACTIVITY_NOT_FOUND));

        // 예약 가능한 날짜 조회
        List<ReserveDate> reserveDates = reserveDateRepository.findAllByActivity(activity);

        // 예약 가능한 날짜인지 확인
        ReserveDate selectedReserveDate = reserveDates.stream()
                .filter(date -> date.getReserveDate().equals(activityReserveRequestDto.reserveDate()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorMessage.INVALID_RESERVE_DATE));

        log.info("selectedReserveDate: {}", selectedReserveDate);

        // 예약 가능한 시간 조회
        List<ReserveTime> reserveTimes = reserveDates.stream()
                .flatMap(reserveDate -> reserveTimeRepository.findAllByReserveDate(reserveDate).stream())
                .toList();
        log.info("reserveTimes: {}", reserveTimes);
        // 예약 가능한 시간인지 확인
        ReserveTime selectedReserveTime = reserveTimes.stream()
                .filter(time -> time.getReserveTime().equals(activityReserveRequestDto.reserveTime()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorMessage.INVALID_RESERVE_TIME));

        // 예약 가능한 인원 조회
        List<ReserveParticipants> reserveParticipantsList = reserveTimes.stream()
                .flatMap(reserveTime -> reserveParticipantsRepository.findByReserveTime(reserveTime).stream())
                .toList();

        int requestedParticipants = activityReserveRequestDto.reserveParticipants(); // DTO에서 요청된 인원수 가져오기
        // 해당 예약 시간의 예약 가능한 인원을 가져오기
        ReserveParticipants reserveParticipants = reserveParticipantsList.stream()
                .findFirst() // 일반적으로 하나의 ReserveTime에 하나의 ReserveParticipants가 있다고 가정합니다.
                .orElseThrow(() -> new BusinessException(ErrorMessage.INVALID_RESERVE_PARTICIPANTS));

        // 남은 예약 가능한 인원이 요청된 인원 수보다 크거나 같은지 확인
        if (reserveParticipants.getRemainParticipants() >= requestedParticipants) {
            // 남은 인원에서 요청된 인원만큼 감소시키기
            reserveParticipants.updateRemainParticipants(reserveParticipants.getRemainParticipants() - requestedParticipants);
            reserveParticipantsRepository.save(reserveParticipants);
        }

        //예약 생성
        Reserve reserve = Reserve.builder()
                .generalMember(generalMember)
                .activity(activity)
                .reserveState("reserved")
                .reserveDate(selectedReserveDate)
                .reserveTime(selectedReserveTime)
                .reservedParticipants(requestedParticipants)
                .reserveDate(selectedReserveDate)
                .reservedAt(LocalDateTime.now())
                .reserveTime(selectedReserveTime)
                .reservedParticipants(requestedParticipants)
                .build();

        reserveRepository.save(reserve);

        return ActivityReserveResponseDto.of(
                reserve.getGeneralMember().getGeneralMemberId(),
                reserve.getReserveId(),
                reserve.getActivity().getActivityId(),
                reserve.getReserveDate().getReserveDate(),
                reserve.getReserveTime().getReserveTime(),
                reserve.getReservedParticipants(),
                reserveParticipants.getRemainParticipants(),
                reserve.getReserveState(),
                reserve.getReservedAt()
        );
    }

    // 예약 삭제
    public SuccessMessage deleteReservation(String token, Long reserveId) {
        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        // 예약 조회
        Reserve reserve = reserveRepository.findById(reserveId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.INVALID_RESERVE));

        // 예약한 사용자가 동일한지 확인
        if (!reserve.getGeneralMember().equals(generalMember)) {
            throw new BusinessException(ErrorMessage.INVALID_RESERVE);
        }

        // 예약 삭제
        try {
            reserveRepository.delete(reserve);
        } catch (Exception e) {
            log.error("Reserve 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorMessage.DATABASE_ERROR);
        }

        return SuccessMessage.RESERVE_ACTIVITY_DELETE_SUCCESS;
    }

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
