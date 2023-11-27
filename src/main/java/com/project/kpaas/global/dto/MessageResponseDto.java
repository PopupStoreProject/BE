package com.project.kpaas.global.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MessageResponseDto {

    private int status;
    private String message;

    @Builder
    private MessageResponseDto(String message, int status) {
        this.status = status;
        this.message = message;
    }

    public static MessageResponseDto of(String message, HttpStatus status) {
        return MessageResponseDto.builder()
                .status(status.value())
                .message(message)
                .build();
    }

}
