package com.project.kpaas.popup.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PopupMsgResponseDto {

    private int status;
    private String message;
    private Long popupId;

    @Builder
    private PopupMsgResponseDto(int status, String message, Long popupId){
        this.status = status;
        this.message = message;
        this.popupId = popupId;
    }

    public static PopupMsgResponseDto of(int status, String message, Long popupId){
        return PopupMsgResponseDto.builder()
                .status(status)
                .message(message)
                .popupId(popupId)
                .build();
    }
}
