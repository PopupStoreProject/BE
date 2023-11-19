package com.project.kpaas.global.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponseDto {

    private int status;
    private String message;

    @Builder
    private SuccessResponseDto(String message, int status) {
        this.status = status;
        this.message = message;
    }

    public static SuccessResponseDto of(String message, HttpStatus status) {
        return SuccessResponseDto.builder()
                .status(status.value())
                .message(message)
                .build();
    }

}
