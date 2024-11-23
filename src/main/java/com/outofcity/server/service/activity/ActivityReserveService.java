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

        List<ReserveDate> reserveDates = reserveDateRepository.findAllByActivity(activity);

        // 예약 가능한 날짜가 있는지 확인
        ReserveDate selectedReserveDate = reserveDates.stream()
                .filter(date -> date.getReserveDate().equals(activityReserveRequestDto.reserveDate().toLocalDate()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorMessage.INVALID_RESERVE_DATE));

        List<ReserveTime> reserveTimes = reserveTimeRepository.findAllByReserveDate(selectedReserveDate);

        // 예약 가능한 시간인지 확인
        ReserveTime selectedReserveTime = reserveTimes.stream()
                .filter(time -> time.getReserveTime().toLocalTime().equals(activityReserveRequestDto.reserveTime().toLocalTime()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorMessage.INVALID_RESERVE_TIME));

        ReserveParticipants reservedParticipants = reserveParticipantsRepository.findByReserveTime(selectedReserveTime)
                .orElseThrow(() -> new BusinessException(ErrorMessage.INVALID_PARTICIPANTS));

        Long activityIdVar = activity.getActivityId();

        String state = "reserved";

        Integer price = activity.getPrice();

//        LocalDate reservedDateVar = selectedReserveDate.getReserveDate();
//
//        Time reservedTimeVar = selectedReserveTime.getReserveTime();

        // 예약자 인원
        Integer MaxreserveParticipantsVar = reservedParticipants.getMaxParticipants();

        Integer remainParticipantsVar = MaxreserveParticipantsVar - activityReserveRequestDto.reserveParticipants();

        // 예약 가능한 인원이 충분한지 확인
        if (activityReserveRequestDto.reserveParticipants() > remainParticipantsVar) {
            throw new BusinessException(ErrorMessage.INVALID_PARTICIPANTS);
        }

        // 예약 엔티티 생성 및 저장
        Reserve reserve = Reserve.of(
                activity,
                generalMember,
                selectedReserveTime,
                selectedReserveDate,
                LocalDateTime.now(),
                activityReserveRequestDto.reserveParticipants(),
                "reserved"
        );

        try {
            reserveRepository.save(reserve);
        } catch (Exception e) {
            log.error("Reserve 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorMessage.DATABASE_ERROR);
        }

        // 예약 정보 반환
        return ActivityReserveResponseDto.of(
                generalMemberId,
                reserve.getReserveId(),
                activity.getActivityId(),
                selectedReserveDate.getReserveDate().atStartOfDay(),
                selectedReserveTime.getReserveTime().toLocalTime().atDate(selectedReserveDate.getReserveDate()),
                activity.getPrice(),
                activityReserveRequestDto.reserveParticipants(),
                remainParticipantsVar,
                "reserved",
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
