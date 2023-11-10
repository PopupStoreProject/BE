package com.project.kpaas.global.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MessageResponseDto {

    private String msg;
    private int statusCode;

    @Builder
    private MessageResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

    public static MessageResponseDto of(String msg, HttpStatus status) {
        return MessageResponseDto.builder()
                .msg(msg)
                .statusCode(status.value())
                .build();
    }

}
