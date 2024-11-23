package com.outofcity.server.service.admin;

import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.domain.UserChallenge;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.repository.GeneralMemberRepository;
import com.outofcity.server.repository.UserChallengeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserChallengeRepository userChallengeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GeneralMemberRepository generalMemberRepository;

    public void proofChallenge(String token, Long userChallengeId) {
        // 관리자 권한 확인 (지금은 관리자 계정이 따로 없으므로 GeneralMember로 대체)
        // Token 검증 및 조회
        GeneralMember generalMember = generalMemberRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER));

        // 사용자 챌린지 가져오기
        UserChallenge userChallenge = userChallengeRepository.findById(userChallengeId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER_CHALLENGE));

        try {
            userChallenge.adminUpdateUserChallenge("certified");
        }
        catch (Exception e) {
            throw new BusinessException(ErrorMessage.FAIL_PROOF_CHALLENGE);
        }
    }
}
