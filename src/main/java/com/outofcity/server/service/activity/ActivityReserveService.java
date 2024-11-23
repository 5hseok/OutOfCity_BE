package com.outofcity.server.service.activity;

import com.outofcity.server.domain.*;
import com.outofcity.server.dto.activity.response.ActivityReserveRequestDto;
import com.outofcity.server.dto.activity.response.ActivityReserveResponseDto;
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
import java.util.List;

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
}
