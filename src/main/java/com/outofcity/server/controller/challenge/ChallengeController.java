package com.outofcity.server.controller.challenge;

import com.outofcity.server.dto.challenge.response.ChallengeTodayResponseDto;
import com.outofcity.server.dto.challenge.response.ChallengeUserHistoryResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.challenge.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController {
    private final ChallengeService challengeService;

    // 오늘의 챌린지 조회
    @GetMapping("/today")
    public ResponseEntity<SuccessStatusResponse<ChallengeTodayResponseDto>> getTodayChallenge(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.CHALLENGE_READ_SUCCESS, challengeService.getTodayChallenge(token)));
    }

    // 사용자의 지금까지 챌린지 현황 조회
    @GetMapping("")
    public ResponseEntity<SuccessStatusResponse<List<ChallengeUserHistoryResponseDto>>> getGeneralMemberChallenge(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.CHALLENGE__HISTORY_READ_SUCCESS, challengeService.getGeneralMemberChallengeHistory(token)));
    }

}
