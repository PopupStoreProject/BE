package com.project.kpaas.popup.dto;

import com.project.kpaas.popup.entity.PopupStore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopupResponseDto {

    private Long id;
    private String popupName;
    private String content;
    private String startDate;
    private String endDate;
    private String gps;

    @Builder
    private PopupResponseDto(Long id, String popupName, String content, String startDate, String endDate, String gps) {
        this.id = id;
        this.popupName = popupName;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.gps = gps;
    }

    public static PopupResponseDto of(PopupStore popupStore) {
        return PopupResponseDto.builder()
                .id(popupStore.getId())
                .popupName(popupStore.getPopupName())
                .content(popupStore.getContent())
                .startDate(popupStore.getStartDate())
                .endDate(popupStore.getEndDate())
                .gps(popupStore.getGps())
                .build();
    }
}
