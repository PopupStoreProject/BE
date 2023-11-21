package com.project.kpaas.popup.dto;

import com.project.kpaas.popup.entity.Popupstore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopupResponseDto {

    private Long id;
    private String popupName;
    private String content;
    private String category;
    private String[] hashtags;
    private String startDate;
    private String endDate;
    private Double latitude;   //위도
    private  Double longitude;  //경도
    private String imageUrl;
    private String homepageUrl;

    @Builder
    private PopupResponseDto(Long id, String popupName, String content, String category, String[] hashtags, String startDate, String endDate, Double latitude, Double longitude, String imageUrl, String homepageUrl) {
        this.id = id;
        this.popupName = popupName;
        this.content = content;
        this.category = category;
        this.hashtags = hashtags;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.homepageUrl = homepageUrl;
    }

    public static PopupResponseDto of(Popupstore popupStore, String category, String[] hashtags) {
        return PopupResponseDto.builder()
                .id(popupStore.getId())
                .popupName(popupStore.getPopupName())
                .content(popupStore.getContent())
                .category(category)
                .hashtags(hashtags)
                .startDate(popupStore.getStartDate())
                .endDate(popupStore.getEndDate())
                .latitude(popupStore.getGps().x)
                .longitude(popupStore.getGps().y)
                .imageUrl(popupStore.getImageUrl())
                .homepageUrl(popupStore.getHomepageUrl())
                .build();
    }

}
