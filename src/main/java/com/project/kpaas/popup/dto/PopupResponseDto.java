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
    private String gps;
    private String imageUrl;
    private String homepageUrl;

    @Builder
    private PopupResponseDto(Long id, String popupName, String content, String category, String[] hashtags, String startDate, String endDate, String gps, String imageUrl, String homepageUrl) {
        this.id = id;
        this.popupName = popupName;
        this.content = content;
        this.category = category;
        this.hashtags = hashtags;
        this.startDate = startDate;
        this.endDate = endDate;
        this.gps = gps;
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
                .gps(popupStore.getGps())
                .imageUrl(popupStore.getImageUrl())
                .homepageUrl(popupStore.getHomepageUrl())
                .build();
    }

}
