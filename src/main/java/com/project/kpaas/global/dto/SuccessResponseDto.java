package com.project.kpaas.global.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponseDto {

    private String msg;
    private int statusCode;

    @Builder
    private SuccessResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

    public static SuccessResponseDto of(String msg, HttpStatus status) {
        return SuccessResponseDto.builder()
                .msg(msg)
                .statusCode(status.value())
                .build();
    }

}
