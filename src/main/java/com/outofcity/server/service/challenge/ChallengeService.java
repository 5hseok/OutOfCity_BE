package com.outofcity.server.service.challenge;

import com.outofcity.server.dto.challenge.response.ChallengeTodayResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.global.jwt.JwtValidationType;
import com.outofcity.server.repository.ChallengeRepository;
import com.outofcity.server.domain.Challenge;
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

    public ChallengeTodayResponseDto getTodayChallenge(String token) {

        // Token 검증
        if (!JwtValidationType.VALID_JWT.equals(jwtTokenProvider.validateToken(token))) {
            throw new BusinessException(ErrorMessage.INVALID_JWT_TOKEN);
        }

        // 오늘 날짜에 해당하는 챌린지가 있다면 반환
        LocalDate today = LocalDate.now();
        Optional<Challenge> todayChallenge = challengeRepository.findByCreatedAt(today);

        if (todayChallenge.isPresent()) {
            return new ChallengeTodayResponseDto(
                    todayChallenge.get().getChallengeId(),
                    todayChallenge.get().getContent(),
                    todayChallenge.get().getCreatedAt()
            );
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

        challenge.updateCreatedAt(today);
        challengeRepository.save(challenge);

        // 챌린지 정보를 DTO로 변환하여 반환
        return new ChallengeTodayResponseDto(
                challenge.getChallengeId(),
                challenge.getContent(),
                today
        );
    }
}
