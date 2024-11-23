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
    UNDETERMINED_FILE(HttpStatus.BAD_REQUEST.value(), "식별할 수 없는 파일입니다."),
    DUPLICATE_BUSINESS_MEMBER(HttpStatus.CONFLICT.value(), "이미 가입한 사업자입니다."),
    INVALID_DATE(HttpStatus.NO_CONTENT.value(), "날짜 형식이 잘못되었습니다."),
    DATABASE_ERROR(HttpStatus.CONFLICT.value(), "데이터베이스 오류가 발생했습니다."),
    NOT_FOUND_FAVORITES(HttpStatus.NO_CONTENT.value(), "찜한 액티비티가 없습니다."),
    ACTIVITY_NOT_FOUND(HttpStatus.NO_CONTENT.value(), "액티비티를 찾을 수 없습니다."),
    ACTIVITY_LIKE_NOT_FOUND(HttpStatus.NO_CONTENT.value(), "찜한 액티비티를 찾을 수 없습니다."),
    INVALID_BUSINESS_MEMBER(HttpStatus.FORBIDDEN.value(), "사업자 등록정보를 확인받지 못했습니다."),
    GENERAL_MEMBER_NOT_FOUND(HttpStatus.NO_CONTENT.value(), "일반회원을 찾을 수 없습니다."),
    INVALID_REVIEW(HttpStatus.FORBIDDEN.value(), "리뷰 평점이 잘못되었습니다."),
    NO_AVAILABLE_CHALLENGE(HttpStatus.SERVICE_UNAVAILABLE.value(), "이번 달에 사용 가능한 챌린지가 없습니다."),
    INVALID_PARTICIPANTS(HttpStatus.BAD_REQUEST.value(), "예약자 수설정이 잘못되었습니다."),
    INVALID_RESERVE_PARTICIPANTS(HttpStatus.BAD_REQUEST.value(), "예약자가 남은 인원보다 많습니다."),
    INVALID_RESERVE_DATE(HttpStatus.BAD_REQUEST.value(), "예약 가능한 날짜가 아닙니다."),
    INVALID_RESERVE_TIME(HttpStatus.BAD_REQUEST.value(), "예약 가능한 시간이 아닙니다."),
    INVALID_RESERVE(HttpStatus.NO_CONTENT.value(), "예약한 액티비티가 없습니다."),
    CART_NOT_FOUND(HttpStatus.NO_CONTENT.value(), "장바구니가 없습니다."),
    CART_NOT_BELONG_TO_MEMBER(HttpStatus.BAD_REQUEST.value(), "해당 사용자의 장바구니가 아닙니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NO_CONTENT.value(), "장바구니에 액티비티가 없습니다."),
    INVALID_DATA(HttpStatus.BAD_REQUEST.value(), " 데이터 무결성 오류 발생"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST.value(), " 유효하지 않은 토큰입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST.value(), " 유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST.value(), " 만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST.value(), " 지원되지 않은 토큰입니다."),
    EMPTY_TOKEN(HttpStatus.BAD_REQUEST.value(), " 빈 토큰입니다."),
    NOT_FOUND_CHALLENGE(HttpStatus.NO_CONTENT.value(), "챌린지를 찾을 수 없습니다."),
    NOT_FOUND_USER_CHALLENGE(HttpStatus.NO_CONTENT.value(), "사용자의 챌린지를 찾을 수 없습니다."),
    ALREADY_CERTIFIED(HttpStatus.CONTINUE.value(), "이미 인증된 챌린지입니다."),
    FAIL_PROOF_CHALLENGE(HttpStatus.FORBIDDEN.value(), "챌린지 인증에 실패했습니다."),
    NOT_FOUND_BUSINESS_MEMBER(HttpStatus.NO_CONTENT.value(), "사업자를 찾을 수 없습니다."),
    NOT_FOUND_SUB_CATEGORY(HttpStatus.NO_CONTENT.value(), "서브 카테고리를 찾을 수 없습니다."),;
    private final int code;
    private final String message;
}
