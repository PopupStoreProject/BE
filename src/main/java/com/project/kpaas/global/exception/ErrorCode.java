package com.project.kpaas.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    AUTHORIZATION(HttpStatus.BAD_REQUEST, "관리자만 작성할 수 있습니다."),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "중복된 아이디가 있습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    NOT_FOUND_POPUP(HttpStatus.BAD_REQUEST, "해당 팝업스토어 찾을 수 없습니다."),
    NOT_FOUND_REGION(HttpStatus.BAD_REQUEST, "해당 지역을 찾을 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임이 있습니다."),

    INVALID_ADMIN_TOKEN(HttpStatus.BAD_REQUEST, "유효한 관리자 토큰이 아닙니다."),

    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "토큰을 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효한 토큰이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String msg;

}
