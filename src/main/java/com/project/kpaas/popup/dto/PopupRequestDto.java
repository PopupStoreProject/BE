package com.project.kpaas.popup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopupRequestDto {
    private String popupName;
    private String content;
    private String category;
    private String[] hashtags;
    private String startDate;
    private String endDate;
    private String openingHours;
    private String gps;
    private String regionName;
    private String imageUrl;
    private String homepageUrl;
}
