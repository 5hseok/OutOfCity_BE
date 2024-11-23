package com.outofcity.server.controller.member;

import com.outofcity.server.dto.member.kakaologin.response.SuccessLoginResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.dto.oauth.KakaoUserInfoRequestDto;
import com.outofcity.server.global.exception.dto.oauth.KakaoUserInfoResponseDto;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.member.GeneralMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GeneralMemberController {

    private final GeneralMemberService generalMemberService;

    //카카오 로그인 메소드
    @PostMapping("/login/kakao")
    public ResponseEntity<SuccessStatusResponse<SuccessLoginResponseDto>> login(@RequestBody KakaoUserInfoRequestDto userInfo) {
        // 3. 사용자 로그인 또는 회원가입 처리
        SuccessLoginResponseDto successLoginResponseDto = generalMemberService.findUserWithReact(userInfo);

        // 5. 메인 페이지로 리다이렉트
        return ResponseEntity.status(HttpStatus.OK).body(
                SuccessStatusResponse.of(
                        SuccessMessage.SIGNIN_SUCCESS, successLoginResponseDto
                )
        );
    }

    //일반 사용자의 여행 등급을 가져오는 메소드
    @GetMapping("/rank")
    public ResponseEntity<SuccessStatusResponse<String>> getRank(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(
                SuccessStatusResponse.of(
                        SuccessMessage.RANK_GET_SUCCESS, generalMemberService.getRank(token)
                )
        );
    }

    //일반 사용자의 닉네임을 변경하는 메소드
    @PatchMapping("/profile")
    public ResponseEntity<SuccessStatusResponse<SuccessLoginResponseDto>> changeNickname(@RequestHeader("Authorization") String token, @RequestParam String nickname) {
        return ResponseEntity.status(HttpStatus.OK).body(
                SuccessStatusResponse.of(
                        SuccessMessage.NICKNAME_CHANGE_SUCCESS, generalMemberService.changeNickname(token, nickname)
                )
        );
    }


//    //카카오 로그인을 한번에 하는 메소드 (사용 X)
//    @GetMapping("/callback")
//    public ResponseEntity<SuccessStatusResponse<SuccessLoginResponseDto>> callback(@RequestParam("code") String code) {
//        // 1. 코드로 Access Token 발급
//        String accessToken = generalMemberService.getAccessTokenFromKakao(code);
//        log.info("accessToken: {}", accessToken);
//        // 2. Access Token으로 사용자 정보 가져오기
//        KakaoUserInfoResponseDto userInfo = generalMemberService.getUserInfo(accessToken);
//
//        // 3. 사용자 로그인 또는 회원가입 처리
//        SuccessLoginResponseDto successLoginResponseDto = generalMemberService.findUser(userInfo);
//
//        // 5. 메인 페이지로 리다이렉트
//        return ResponseEntity.status(HttpStatus.OK).body(
//                SuccessStatusResponse.of(
//                        SuccessMessage.SIGNIN_SUCCESS, successLoginResponseDto
//                )
//        );
//    }

}
