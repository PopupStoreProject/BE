package com.project.kpaas.popup.dto;

import com.project.kpaas.popup.entity.Popupstore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

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
    private Double longitude;  //경도
    private String homepageUrl;
    private String instagramUrl;
    @Nullable
    private Object[] blogReview;

    @Nullable
    private String[] images;

    private String like;
    private String brandImage;
    private String regionName;

    @Builder
    private PopupResponseDto(Long id, String popupName, String content, String category, String[] hashtags, String startDate, String endDate, Double latitude, Double longitude, String homepageUrl, String instagramUrl, Object[] blogReview, String[] images, String like, String brandImage, String regionName) {
        this.id = id;
        this.popupName = popupName;
        this.content = content;
        this.category = category;
        this.hashtags = hashtags;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
        this.homepageUrl = homepageUrl;
        this.instagramUrl = instagramUrl;
        this.blogReview = blogReview;
        this.like = like;
        this.brandImage = brandImage;
        this.regionName = regionName;
    }

    // 전체, 카테고리 조회
    public static PopupResponseDto of(Popupstore popupStore, String category, String[] hashtags, String[] images, String like, String regionName) {
        return PopupResponseDto.builder()
                .id(popupStore.getId())
                .popupName(popupStore.getPopupName())
                .category(category)
                .hashtags(hashtags)
                .startDate(popupStore.getStartDate())
                .endDate(popupStore.getEndDate())
//                .latitude(popupStore.getGps().getX())
//                .longitude(popupStore.getGps().getY())
                .images(images)
                .like(like)
                .regionName(regionName)
//                .homepageUrl(popupStore.getHomepageUrl())
//                .instagramUrl(popupStore.getInstagramUrl())
                .build();
    }

    // 상세 조회
    public static PopupResponseDto of(Popupstore popupStore, String category, String[] hashtags, Object[] blogReview, String[] images, String like, String brandImage, String regionName) {
        return PopupResponseDto.builder()
                .id(popupStore.getId())
                .popupName(popupStore.getPopupName())
                .content(popupStore.getContent())
                .category(category)
                .hashtags(hashtags)
                .startDate(popupStore.getStartDate())
                .endDate(popupStore.getEndDate())
                .latitude(popupStore.getGps().getX())
                .longitude(popupStore.getGps().getY())
                .homepageUrl(popupStore.getHomepageUrl())
                .instagramUrl(popupStore.getInstagramUrl())
                .blogReview(blogReview)
                .images(images)
                .like(like)
                .brandImage(brandImage)
                .regionName(regionName)
                .build();
    }

}
