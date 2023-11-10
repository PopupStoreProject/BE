package com.project.kpaas.popup.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageResponseDto {

    private int status;
    private String message;
    private Long popupId;

    @Builder
    private MessageResponseDto(int status, String message, Long popupId){
        this.status = status;
        this.message = message;
        this.popupId = popupId;
    }

    public static MessageResponseDto of(int status, String message, Long popupId){
        return MessageResponseDto.builder()
                .status(status)
                .message(message)
                .popupId(popupId)
                .build();
    }
}
