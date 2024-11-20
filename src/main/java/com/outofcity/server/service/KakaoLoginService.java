package com.outofcity.server.service;

import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.dto.member.kakaologin.response.SuccessLoginResponseDto;
import com.outofcity.server.global.exception.dto.oauth.KakaoTokenResponseDto;
import com.outofcity.server.global.exception.dto.oauth.KakaoUserInfoResponseDto;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.global.jwt.UserAuthentication;
import com.outofcity.server.repository.GeneralMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.http.HttpClient;
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class KakaoLoginService {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    private static final String KAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAPI_USER_URL = "https://kapi.kakao.com/v2/user/me";
    private final GeneralMemberRepository generalMemberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 1. Access Token 받기
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

    // 2. 사용자 정보 가져오기
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        KakaoUserInfoResponseDto userInfo = WebClient.create(KAPI_USER_URL)
                .get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        return userInfo;
    }

    public SuccessLoginResponseDto findUser(KakaoUserInfoResponseDto userInfo, String token) {

        GeneralMember generalMember = generalMemberRepository.findById(userInfo.getId())
                .orElseGet(() -> {
                    GeneralMember newGeneralMember = GeneralMember.builder()
                            .id(userInfo.getId())
                            .name(userInfo.getProperties().getNickname())
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
