package com.project.kpaas.popup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopupRequestDto {
    private String popupName;
    private String content;
    private String startDate;
    private String endDate;
    private String gps;
    private String regionName;
}
