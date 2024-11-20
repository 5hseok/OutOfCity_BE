package com.outofcity.server.controller.member;

import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.dto.member.kakaologin.response.SuccessLoginResponseDto;
import com.outofcity.server.global.exception.dto.oauth.KakaoUserInfoResponseDto;
import com.outofcity.server.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        // 1. 코드로 Access Token 발급
        String accessToken = kakaoLoginService.getAccessTokenFromKakao(code);
        log.info("accessToken: {}", accessToken);
        // 2. Access Token으로 사용자 정보 가져오기
        KakaoUserInfoResponseDto userInfo = kakaoLoginService.getUserInfo(accessToken);

        // 3. 사용자 로그인 또는 회원가입 처리
        SuccessLoginResponseDto successLoginResponseDto = kakaoLoginService.findUser(userInfo, accessToken);

        // 5. 메인 페이지로 리다이렉트
        return ResponseEntity.ok(successLoginResponseDto);
    }
}
