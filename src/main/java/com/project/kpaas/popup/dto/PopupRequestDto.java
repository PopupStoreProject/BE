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
    private Double latitude;   //위도
    private Double longitude;  //경도
    private String regionName;
    private String[] images;
    private String homepageUrl;
    private String instagramUrl;
}
