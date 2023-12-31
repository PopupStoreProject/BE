package com.project.kpaas.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Auth
    AUTHORIZATION(HttpStatus.FORBIDDEN, "관리자만 작성할 수 있습니다."),
    INVALID_ADMIN_TOKEN(HttpStatus.UNAUTHORIZED, "유효한 관리자 토큰이 아닙니다."),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    NOT_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // Duplication
    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "중복된 아이디가 있습니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임이 있습니다."),


    // Not Found
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    NOT_FOUND_POPUP(HttpStatus.NOT_FOUND, "해당 팝업스토어를 찾을 수 없습니다."),
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "해당 이미지를 찾을 수 없습니다."),
    NOT_FOUND_BRAND_IMAGE(HttpStatus.NOT_FOUND, "해당 브랜드 이미지를 찾을 수 없습니다."),
    NOT_FOUND_NEAR_POPUP(HttpStatus.NOT_FOUND, "해당 반경 내 팝업스토어를 찾을 수 없습니다."),
    NOT_FOUND_BRAND(HttpStatus.NOT_FOUND, "해당 브랜드를 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
    NOT_FOUND_HASHTAG(HttpStatus.NOT_FOUND, "해당 해시태그를 찾을 수 없습니다."),
    NOT_FOUND_REGION(HttpStatus.NOT_FOUND, "해당 지역을 찾을 수 없습니다."),

    NOT_FOUND_EDITION(HttpStatus.NOT_FOUND, "수정할 카테고리를 하나 이상 지정해 주세요.");


    private final HttpStatus httpStatus;
    private final String message;

}
