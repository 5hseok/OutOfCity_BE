package com.outofcity.server.service.challenge;

import com.outofcity.server.domain.Challenge;
import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.domain.UserChallenge;
import com.outofcity.server.dto.challenge.request.ChallengeRegisterImageRequestDto;
import com.outofcity.server.dto.challenge.response.ChallengeTodayResponseDto;
import com.outofcity.server.dto.challenge.response.ChallengeUserHistoryResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.repository.ChallengeRepository;
import com.outofcity.server.repository.GeneralMemberRepository;
import com.outofcity.server.repository.UserChallengeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GeneralMemberRepository generalMemberRepository;
    private final UserChallengeRepository userChallengeRepository;

    //오늘 챌린지 조회
    public ChallengeTodayResponseDto getTodayChallenge(String token) {

        //로그인 되지 않은 사용자라면 오늘 날짜에 해당하는 챌린지 반환
        if(token == null){

            // 오늘 날짜에 해당하는 챌린지가 있다면 반환
            LocalDate today = LocalDate.now();
            Optional<Challenge> todayChallenge = challengeRepository.findByCreatedAt(today);

            return new ChallengeTodayResponseDto(
                    todayChallenge.get().getChallengeId(),
                    todayChallenge.get().getContent(),
                    todayChallenge.get().getCreatedAt()
            );
        }

        // 로그인 된 사용자 코드
        // Token 검증 및 일반회원 조회
        GeneralMember generalMember = generalMemberRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER));

        // 오늘 날짜에 해당하는 챌린지가 있다면 반환
        LocalDate today = LocalDate.now();
        Optional<Challenge> todayChallenge = challengeRepository.findByCreatedAt(today);

        if (todayChallenge.isPresent()) {
            // userChallenge 테이블에 todayChallenge에 해당하는 날짜를 가진 챌린지가 있는지 확인
            try {
                Optional<UserChallenge> existingUserChallenge = userChallengeRepository.findByGeneralMemberAndPerformedAt(
                        generalMember,
                        todayChallenge.get().getCreatedAt()
                );

                log.info("existingUserChallenge : {}", existingUserChallenge);

                if (existingUserChallenge.isEmpty()) {

                    //userChallenge에 챌린지 추가
                    UserChallenge userChallenge = UserChallenge.of(
                            generalMember,
                            todayChallenge.get(),
                            null,
                            "uncertified",
                            todayChallenge.get().getCreatedAt()
                    );

                    // userChallenge 저장
                    userChallengeRepository.save(userChallenge);
                }

                //userChallenge 반환
                return new ChallengeTodayResponseDto(
                        todayChallenge.get().getChallengeId(),
                        todayChallenge.get().getContent(),
                        todayChallenge.get().getCreatedAt()
                );
            }
            catch (Exception e) {
                throw new BusinessException(ErrorMessage.NOT_FOUND_USER_CHALLENGE);
            }
        }

        // 오늘 날짜에 해당하는 챌린지가 없다면 새로운 챌린지 생성
        // 지난 달에 사용한 챌린지 중 현재 날짜와 7일 이상 차이 나는 것 중 하나 무작위로 가져오기
        LocalDate oneMonthAgo = today.minusMonths(1);

        // 지난 달에 사용된 챌린지 목록 조회
        List<Challenge> challenges = challengeRepository.findAll().stream()
                .filter(challenge -> challenge.getCreatedAt() == null ||
                        challenge.getCreatedAt().getMonthValue() == oneMonthAgo.getMonthValue() &&
                        challenge.getCreatedAt().plusDays(7).isBefore(today))
                .toList();

        if (challenges.isEmpty()) {
            throw new BusinessException(ErrorMessage.NO_AVAILABLE_CHALLENGE);
        }

        //현재 챌린지 목록 로그 출력
        log.info("현재 챌린지 목록 : {}", challenges.stream().map(Challenge::getChallengeId).collect(Collectors.toList()));


        // 무작위로 하나 선택
        Random random = new Random();
        Challenge challenge = challenges.get(random.nextInt(challenges.size()));

        //챌린지 정보 업데이트
        challenge.updateCreatedAt(today);
        challengeRepository.save(challenge);

        //userChallenge에 챌린지 추가
        UserChallenge userChallenge = UserChallenge.of(
                generalMember,
                challenge,
                null,
                "uncertified",
                today
        );

        //userChallenge 저장
        userChallengeRepository.save(userChallenge);

        // 챌린지 정보를 DTO로 변환하여 반환
        return new ChallengeTodayResponseDto(
                challenge.getChallengeId(),
                challenge.getContent(),
                today
        );
    }

    public List<ChallengeUserHistoryResponseDto> getGeneralMemberChallengeHistory(String token) {

        // Token 검증 및 일반회원 조회
        GeneralMember generalMember = generalMemberRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER));

        // 일반회원의 챌린지 목록 조회
        List<UserChallenge> userChallengeHistory = userChallengeRepository.findAllByGeneralMember(generalMember);

        // 챌린지 정보를 DTO로 변환하여 반환
        return userChallengeHistory.stream()
                .map(userChallenge -> ChallengeUserHistoryResponseDto.builder()
                        .id(userChallenge.getUserChallengeId())
                        .image_url(userChallenge.getImageUrl())
                        .content(userChallenge.getChallenge().getContent())
                        .performedAt(userChallenge.getChallenge().getCreatedAt().toString())
                        .certification(userChallenge.getCertification())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public void proofChallenge(String token, ChallengeRegisterImageRequestDto requestDto) {

        // Token 검증 및 일반회원 조회
        GeneralMember generalMember = generalMemberRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER));

        // userChallenge 테이블에서 조회
        UserChallenge userChallenge = userChallengeRepository.findByGeneralMemberAndPerformedAt(generalMember, LocalDate.now())
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER_CHALLENGE));

        // 이미 인증된 챌린지인 경우
        if (userChallenge.getCertification().equals("certified")) {
            throw new BusinessException(ErrorMessage.ALREADY_CERTIFIED);
        }

        // 인증 중으로 변경
        userChallenge.userUpdateUserChallenge(requestDto.imageUrl());

        // userChallenge 저장
        userChallengeRepository.save(userChallenge);
    }
}
