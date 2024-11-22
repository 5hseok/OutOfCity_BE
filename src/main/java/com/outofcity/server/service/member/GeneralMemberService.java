package com.outofcity.server.service.member;

import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.dto.member.kakaologin.response.SuccessLoginResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.dto.oauth.KakaoTokenResponseDto;
import com.outofcity.server.global.exception.dto.oauth.KakaoUserInfoRequestDto;
import com.outofcity.server.global.exception.dto.oauth.KakaoUserInfoResponseDto;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.global.jwt.UserAuthentication;
import com.outofcity.server.repository.GeneralMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class GeneralMemberService {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    private static final String KAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAPI_USER_URL = "https://kapi.kakao.com/v2/user/me";
    private final GeneralMemberRepository generalMemberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 사용자 정보 가져오기 - 이메일, 닉네임, 프로필 사진, 여행 등급
    public SuccessLoginResponseDto findUserWithReact(KakaoUserInfoRequestDto userInfo) {

        GeneralMember generalMember = generalMemberRepository.findById(userInfo.id())
                .orElseGet(() -> {
                    GeneralMember newGeneralMember = GeneralMember.builder()
                            .id(userInfo.id())
                            .name(userInfo.name())
                            .email(userInfo.email())
                            .rank("여행 씨앗")
                            .profileImageUrl(userInfo.profileImageUrl())
                            .build();
                    return generalMemberRepository.save(newGeneralMember);
                });

        // JWT 토큰 발급
        String jwtToken = jwtTokenProvider.issueAccessToken(
                UserAuthentication.createUserAuthentication(generalMember.getGeneralMemberId())
        );

        return SuccessLoginResponseDto.builder()
                .id(generalMember.getGeneralMemberId())
                .name(generalMember.getName())
                .nickname(generalMember.getNickname())
                .rank(generalMember.getRank())
                .profileImageUrl(generalMember.getProfileImageUrl())
                .email(generalMember.getEmail())
                .jwtToken(jwtToken)
                .build();
    }

    // 일반 사용자의 여행 등급을 가져오는 메소드
    public String getRank(String token) {
        GeneralMember generalMember = generalMemberRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        return generalMember.getRank();
    }

    // 일반 사용자의 닉네임을 변경하는 메소드
    public SuccessLoginResponseDto changeNickname(String token, String nickname) {
        GeneralMember generalMember = generalMemberRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        generalMember.userUpdateGeneralMember(nickname);

        return SuccessLoginResponseDto.builder()
                .id(generalMember.getGeneralMemberId())
                .name(generalMember.getName())
                .nickname(generalMember.getNickname())
                .rank(generalMember.getRank())
                .profileImageUrl(generalMember.getProfileImageUrl())
                .email(generalMember.getEmail())
                .jwtToken(token)
                .build();
    }

    // Kakao Access Token 받기
    public String getAccessTokenFromKakao(String code){
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        return kakaoTokenResponseDto.getAccessToken();
    }

    //  사용자 정보 가져오기 - 이메일, 닉네임, 프로필 사진
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        KakaoUserInfoResponseDto userInfo = WebClient.create(KAPI_USER_URL)
                .get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        return userInfo;
    }

    // 사용자 정보 가져오기 - 이메일, 닉네임, 프로필 사진, 여행 등급
    public SuccessLoginResponseDto findUser(KakaoUserInfoResponseDto userInfo) {

        GeneralMember generalMember = generalMemberRepository.findById(userInfo.getId())
                .orElseGet(() -> {
                    GeneralMember newGeneralMember = GeneralMember.builder()
                            .id(userInfo.getId())
                            .name(userInfo.getProperties().getNickname())
                            .email(userInfo.getKakaoAccount().getEmail())
                            .rank("여행 씨앗")
                            .profileImageUrl(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                            .build();
                    return generalMemberRepository.save(newGeneralMember);
                });

        // JWT 토큰 발급
        String jwtToken = jwtTokenProvider.issueAccessToken(
                UserAuthentication.createUserAuthentication(generalMember.getGeneralMemberId())
        );

        return SuccessLoginResponseDto.builder()
                .id(generalMember.getGeneralMemberId())
                .name(generalMember.getName())
                .rank(generalMember.getRank())
                .profileImageUrl(generalMember.getProfileImageUrl())
                .email(generalMember.getEmail())
                .jwtToken(jwtToken)
                .build();
    }
}