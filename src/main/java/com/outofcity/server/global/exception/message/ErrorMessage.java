package com.outofcity.server.global.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    NOT_FOUND_USER(HttpStatus.NO_CONTENT.value(), "이메일에 해당하는 사용자가 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT.value(), "이미 가입한 사용자 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    JWT_UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "사용자의 로그인 검증을 실패했습니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 accessToken입니다."),
    ALREADY_LOGOUT_TOKEN(HttpStatus.UNAUTHORIZED.value(), "이미 로그아웃된 토큰입니다."),
    DEACTIVATED_ACCOUNT(HttpStatus.FORBIDDEN.value(), "탈퇴된 계정입니다."),
    NOT_SUPPORTED_MEDIA_TYPE_ERROR(HttpStatus.BAD_REQUEST.value(), "지원하지 않는 파일 형식입니다."),
    BAD_IMAGE_FILE(HttpStatus.BAD_REQUEST.value(), "파일에 문제가 있습니다."),
    UNDETERMINED_FILE(HttpStatus.BAD_REQUEST.value(), "식별할 수 없는 파일입니다."),;
    private final int code;
    private final String message;
}